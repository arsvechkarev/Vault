package com.arsvechkarev.vault.cryptography

import com.arsvechkarev.vault.core.FileSaver
import com.arsvechkarev.vault.core.JSON_SERVICE_EMAIL
import com.arsvechkarev.vault.core.JSON_SERVICE_ID
import com.arsvechkarev.vault.core.JSON_SERVICE_NAME
import com.arsvechkarev.vault.core.JSON_SERVICE_PASSWORD
import com.arsvechkarev.vault.core.JSON_SERVICE_USERNAME
import com.arsvechkarev.vault.core.JsonConverter
import com.arsvechkarev.vault.core.PASSWORDS_FILENAME
import com.arsvechkarev.vault.core.extensions.assertThat
import com.arsvechkarev.vault.core.model.Service

class ServicesStorageImpl(
  private val cryptography: Cryptography,
  private val fileSaver: FileSaver,
  private val jsonConverter: JsonConverter
) : ServicesStorage {
  
  override fun getServices(password: String): List<Service> {
    val ciphertext = fileSaver.readTextFromFile(PASSWORDS_FILENAME)
    // Text in file should not be empty, because it was saved earlier
    assertThat(ciphertext.isNotEmpty())
    val metaInfo = cryptography.getEncryptionMetaInfo(password, ciphertext)
    val json = cryptography.decryptCipher(password, metaInfo, ciphertext)
    if (json == "") return ArrayList()
    return getFromString(json)
  }
  
  override fun saveService(password: String, service: Service) {
    val servicesList = getServices(password).toMutableList()
    servicesList.add(service)
    saveServicesToFile(servicesList, password)
  }
  
  override fun updateService(password: String, service: Service) {
    val servicesList = getServices(password).toMutableList()
    for (i in servicesList.indices) {
      if (service.id == servicesList[i].id) {
        servicesList[i] = service
        break
      }
    }
    saveServicesToFile(servicesList, password)
  }
  
  override fun deleteService(password: String, service: Service) {
    val servicesInfoList = getServices(password).toMutableList()
    val oldSize = servicesInfoList.size
    servicesInfoList.removeIf { it.id == service.id }
    assertThat(servicesInfoList.size == oldSize - 1)
    saveServicesToFile(servicesInfoList, password)
  }
  
  private fun saveServicesToFile(servicesList: MutableList<Service>, password: String) {
    val servicesInfoJson = convertToString(servicesList)
    val ciphertext = fileSaver.readTextFromFile(PASSWORDS_FILENAME)
    val metaInfo = cryptography.getEncryptionMetaInfo(password, ciphertext)
    val encryptedText = cryptography.encryptData(password, metaInfo, servicesInfoJson)
    fileSaver.saveTextToFile(PASSWORDS_FILENAME, encryptedText)
  }
  
  private fun getFromString(json: String): List<Service> {
    return jsonConverter.getFromString(json, mapper = { map ->
      Service(
        map.getValue(JSON_SERVICE_ID),
        map.getValue(JSON_SERVICE_NAME),
        map.getValue(JSON_SERVICE_USERNAME),
        map.getValue(JSON_SERVICE_EMAIL),
        map.getValue(JSON_SERVICE_PASSWORD)
      )
    })
  }
  
  private fun convertToString(list: List<Service>): String {
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