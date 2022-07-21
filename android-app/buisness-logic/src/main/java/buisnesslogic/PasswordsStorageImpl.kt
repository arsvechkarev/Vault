package buisnesslogic

import buisnesslogic.model.PasswordInfo

class PasswordsStorageImpl(
  private val cryptography: Cryptography,
  private val fileSaver: FileSaver,
  private val jsonConverter: JsonConverter
) : PasswordsStorage {
  
  override suspend fun getPasswords(masterPassword: String): List<PasswordInfo> {
    val ciphertext = checkNotNull(fileSaver.readData()) { "Encrypted data was empty" }
    // Text in file should not be empty, because it should have been saved earlier
    require(ciphertext.isNotEmpty())
    val json = cryptography.decryptData(masterPassword, ciphertext)
    if (json == "") return ArrayList()
    return getFromString(json)
  }
  
  override suspend fun savePassword(masterPassword: String, passwordInfo: PasswordInfo) {
    val passwords = getPasswords(masterPassword).toMutableList()
    passwords.add(passwordInfo)
    savePasswordsToFile(passwords, masterPassword)
  }
  
  override suspend fun updatePasswordInfo(masterPassword: String, passwordInfo: PasswordInfo) {
    val passwords = getPasswords(masterPassword).toMutableList()
    for (i in passwords.indices) {
      if (passwordInfo.id == passwords[i].id) {
        passwords[i] = passwordInfo
        break
      }
    }
    savePasswordsToFile(passwords, masterPassword)
  }
  
  override suspend fun deletePassword(masterPassword: String, passwordInfo: PasswordInfo) {
    val passwords = getPasswords(masterPassword).toMutableList()
    val oldSize = passwords.size
    removePasswordById(passwords, passwordInfo.id)
    require(passwords.size == oldSize - 1)
    savePasswordsToFile(passwords, masterPassword)
  }
  
  private suspend fun savePasswordsToFile(servicesList: MutableList<PasswordInfo>, password: String) {
    val passwordsInfoJson = convertToString(servicesList)
    val encryptedText = cryptography.encryptData(password, passwordsInfoJson)
    fileSaver.saveData(encryptedText)
  }
  
  private fun removePasswordById(servicesList: MutableCollection<PasswordInfo>, id: String) {
    val iterator = servicesList.iterator()
    while (iterator.hasNext()) {
      if (iterator.next().id == id) {
        iterator.remove()
      }
    }
  }
  
  private fun getFromString(json: String): List<PasswordInfo> {
    return jsonConverter.getFromString(json, mapper = { map ->
      PasswordInfo(
        map.getValue(KEY_ITEM_ID),
        map.getValue(KEY_WEBSITE_NAME),
        map.getValue(KEY_LOGIN),
        map.getValue(KEY_EMAIL),
        map.getValue(KEY_NOTES)
      )
    })
  }
  
  private fun convertToString(list: List<PasswordInfo>): String {
    return jsonConverter.convertToString(list, converter = { service ->
      mapOf(
        KEY_ITEM_ID to service.id,
        KEY_WEBSITE_NAME to service.websiteName,
        KEY_LOGIN to service.login,
        KEY_EMAIL to service.notes,
        KEY_NOTES to service.password
      )
    })
  }
  
  private companion object {
    
    const val KEY_ITEM_ID = "id"
    const val KEY_WEBSITE_NAME = "website_name"
    const val KEY_LOGIN = "login"
    const val KEY_EMAIL = "email"
    const val KEY_NOTES = "notes"
  }
}