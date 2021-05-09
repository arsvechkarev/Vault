package buisnesslogic

import buisnesslogic.model.ServiceEntity

class ServicesStorageImpl(
  private val cryptography: Cryptography,
  private val fileSaver: FileSaver,
  private val jsonConverter: JsonConverter
) : ServicesStorage {
  
  override fun getServices(password: String): List<ServiceEntity> {
    val ciphertext = fileSaver.readTextFromFile()
    // Text in file should not be empty, because it was saved earlier
    require(ciphertext.isNotEmpty())
    val json = cryptography.decryptData(password, ciphertext)
    if (json == "") return ArrayList()
    return getFromString(json)
  }
  
  override fun saveService(password: String, serviceEntity: ServiceEntity) {
    val servicesList = getServices(password).toMutableList()
    servicesList.add(serviceEntity)
    saveServicesToFile(servicesList, password)
  }
  
  override fun updateService(password: String, serviceEntity: ServiceEntity) {
    val servicesList = getServices(password).toMutableList()
    for (i in servicesList.indices) {
      if (serviceEntity.id == servicesList[i].id) {
        servicesList[i] = serviceEntity
        break
      }
    }
    saveServicesToFile(servicesList, password)
  }
  
  override fun deleteService(password: String, serviceEntity: ServiceEntity) {
    val servicesInfoList = getServices(password).toMutableList()
    val oldSize = servicesInfoList.size
    removeServiceById(servicesInfoList, serviceEntity.id)
    require(servicesInfoList.size == oldSize - 1)
    saveServicesToFile(servicesInfoList, password)
  }
  
  private fun saveServicesToFile(servicesList: MutableList<ServiceEntity>, password: String) {
    val servicesInfoJson = convertToString(servicesList)
    val ciphertext = fileSaver.readTextFromFile()
    val encryptedText = cryptography.encryptData(password, servicesInfoJson, ciphertext)
    fileSaver.saveTextToFile(encryptedText)
  }
  
  private fun removeServiceById(servicesList: MutableCollection<ServiceEntity>, id: String) {
    val iterator = servicesList.iterator()
    while (iterator.hasNext()) {
      if (iterator.next().id == id) {
        iterator.remove()
      }
    }
  }
  
  private fun getFromString(json: String): List<ServiceEntity> {
    return jsonConverter.getFromString(json, mapper = { map ->
      ServiceEntity(
        map.getValue(JSON_SERVICE_ID),
        map.getValue(JSON_SERVICE_NAME),
        map.getValue(JSON_SERVICE_USERNAME),
        map.getValue(JSON_SERVICE_EMAIL),
        map.getValue(JSON_SERVICE_PASSWORD)
      )
    })
  }
  
  private fun convertToString(list: List<ServiceEntity>): String {
    return jsonConverter.convertToString(list, converter = { service ->
      mapOf(
        JSON_SERVICE_ID to service.id,
        JSON_SERVICE_NAME to service.serviceName,
        JSON_SERVICE_USERNAME to service.username,
        JSON_SERVICE_EMAIL to service.email,
        JSON_SERVICE_PASSWORD to service.password
      )
    })
  }
}