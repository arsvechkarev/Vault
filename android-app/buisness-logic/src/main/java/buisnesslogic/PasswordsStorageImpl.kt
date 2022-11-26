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
  
  override suspend fun savePasswords(
    masterPassword: String,
    passwords: List<PasswordInfo>
  ) {
    savePasswordsToFile(masterPassword, passwords.toMutableList())
  }
  
  override suspend fun savePassword(masterPassword: String, passwordInfo: PasswordInfo) {
    val passwords = getPasswords(masterPassword).toMutableList()
    passwords.add(passwordInfo)
    savePasswordsToFile(masterPassword, passwords)
  }
  
  override suspend fun updatePasswordInfo(masterPassword: String, passwordInfo: PasswordInfo) {
    val passwords = getPasswords(masterPassword).toMutableList()
    for (i in passwords.indices) {
      if (passwordInfo.id == passwords[i].id) {
        passwords[i] = passwordInfo
        break
      }
    }
    savePasswordsToFile(masterPassword, passwords)
  }
  
  override suspend fun deletePassword(masterPassword: String, passwordInfo: PasswordInfo) {
    val passwords = getPasswords(masterPassword).toMutableList()
    val oldSize = passwords.size
    removePasswordById(passwords, passwordInfo.id)
    require(passwords.size == oldSize - 1)
    savePasswordsToFile(masterPassword, passwords)
  }
  
  private suspend fun savePasswordsToFile(
    password: String,
    passwordsList: MutableList<PasswordInfo>
  ) {
    val passwordsInfoJson = convertToString(passwordsList)
    val encryptedText = cryptography.encryptData(password, passwordsInfoJson)
    fileSaver.saveData(encryptedText)
  }
  
  private fun removePasswordById(passwordsList: MutableCollection<PasswordInfo>, id: String) {
    val iterator = passwordsList.iterator()
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
    return jsonConverter.convertToString(list, converter = { password ->
      mapOf(
        KEY_ITEM_ID to password.id,
        KEY_WEBSITE_NAME to password.websiteName,
        KEY_LOGIN to password.login,
        KEY_EMAIL to password.notes,
        KEY_NOTES to password.password
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
