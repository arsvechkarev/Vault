package buisnesslogic

import buisnesslogic.model.PasswordInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PasswordsStorageImpl(
  private val cryptography: Cryptography,
  private val fileSaver: FileSaver,
  private val gson: Gson
) : PasswordsStorage {
  
  override suspend fun getPasswords(masterPassword: String): List<PasswordInfo> {
    val ciphertext = checkNotNull(fileSaver.readData()) { "Encrypted data was empty" }
    // Text in file should not be empty, because it should have been saved earlier
    require(ciphertext.isNotEmpty())
    val json = cryptography.decryptData(masterPassword, ciphertext)
    if (json == "") return ArrayList()
    return gson.fromJson(json, type)
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
    val passwordsInfoJson = gson.toJson(passwordsList, type)
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
  
  companion object {
    private val type = TypeToken.getParameterized(List::class.java, PasswordInfo::class.java).type
  }
}
