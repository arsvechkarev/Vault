package com.arsvechkarev.vault.core

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class PasswordFilesSaverImpl(private val context: Context) : PasswordFilesSaver {
  
  override fun getDecryptedPasswordsList(masterPassword: String): JSONArray {
    val encryptedPasswords = FileUtils.readTextFromFile(context, PASSWORDS_FILENAME)
    if (encryptedPasswords == "") return JSONArray()
    val decryptedPasswords = decryptText(masterPassword, encryptedPasswords)
    return JSONArray(decryptedPasswords)
  }
  
  override fun savePassword(masterPassword: String, serviceName: String, servicePassword: String) {
    val passwords = getDecryptedPasswordsList(masterPassword)
    val obj = JSONObject(mapOf(
      JSON_SERVICE_NAME to serviceName,
      JSON_SERVICE_PASSWORD to servicePassword
    ))
    passwords.put(obj)
    val passwordsText = passwords.toString()
    val encryptedText = encryptText(masterPassword, passwordsText)
    FileUtils.saveTextToFile(context, PASSWORDS_FILENAME, encryptedText)
  }
  
  override fun deletePassword(masterPassword: String, serviceName: String) {
    val passwords = getDecryptedPasswordsList(masterPassword)
    for (i in 0 until passwords.length()) {
      if (passwords.getJSONObject(i).getString(JSON_SERVICE_NAME) == serviceName) {
        passwords.remove(i)
        break
      }
    }
    val passwordsText = passwords.toString()
    val encryptedText = encryptText(masterPassword, passwordsText)
    FileUtils.saveTextToFile(context, PASSWORDS_FILENAME, encryptedText)
  }
}