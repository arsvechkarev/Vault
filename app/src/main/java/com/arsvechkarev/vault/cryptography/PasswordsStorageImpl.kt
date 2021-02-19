package com.arsvechkarev.vault.cryptography

import android.content.Context
import com.arsvechkarev.vault.core.JSON_SERVICE_EMAIL
import com.arsvechkarev.vault.core.JSON_SERVICE_ID
import com.arsvechkarev.vault.core.JSON_SERVICE_NAME
import com.arsvechkarev.vault.core.JSON_SERVICE_PASSWORD
import com.arsvechkarev.vault.core.JSON_SERVICE_USERNAME
import com.arsvechkarev.vault.core.PASSWORDS_FILENAME
import com.arsvechkarev.vault.core.model.ServiceInfo
import org.json.JSONArray
import org.json.JSONObject

class PasswordsStorageImpl(private val context: Context) : PasswordsStorage {
  
  override fun getServicesInfoList(masterPassword: String): JSONArray {
    val encryptedPasswords = EncryptedFileUtils.readTextFromFile(context, PASSWORDS_FILENAME)
    if (encryptedPasswords == "") return JSONArray()
    val decryptedPasswords = decryptText(masterPassword, encryptedPasswords)
    return JSONArray(decryptedPasswords)
  }
  
  override fun saveServiceInfo(masterPassword: String, serviceInfo: ServiceInfo) {
    val passwords = getServicesInfoList(masterPassword)
    val obj = JSONObject(mapOf(
      JSON_SERVICE_ID to serviceInfo.id,
      JSON_SERVICE_NAME to serviceInfo.serviceName,
      JSON_SERVICE_USERNAME to serviceInfo.username,
      JSON_SERVICE_EMAIL to serviceInfo.email,
      JSON_SERVICE_PASSWORD to serviceInfo.password
    ))
    passwords.put(obj)
    val passwordsText = passwords.toString()
    val encryptedText = encryptText(masterPassword, passwordsText)
    EncryptedFileUtils.saveTextToFile(context, PASSWORDS_FILENAME, encryptedText)
  }
  
  override fun updateServiceInfo(masterPassword: String, serviceInfo: ServiceInfo) {
    val passwords = getServicesInfoList(masterPassword)
    val obj = JSONObject(mapOf(
      JSON_SERVICE_ID to serviceInfo.id,
      JSON_SERVICE_NAME to serviceInfo.serviceName,
      JSON_SERVICE_USERNAME to serviceInfo.username,
      JSON_SERVICE_EMAIL to serviceInfo.email,
      JSON_SERVICE_PASSWORD to serviceInfo.password
    ))
    for (i in 0 until passwords.length()) {
      if (passwords.getJSONObject(i).getString(JSON_SERVICE_ID) == serviceInfo.id) {
        passwords.put(i, obj)
        break
      }
    }
    val passwordsText = passwords.toString()
    val encryptedText = encryptText(masterPassword, passwordsText)
    EncryptedFileUtils.saveTextToFile(context, PASSWORDS_FILENAME, encryptedText)
  }
  
  override fun deleteServiceInfo(masterPassword: String, serviceInfo: ServiceInfo) {
    val passwords = getServicesInfoList(masterPassword)
    for (i in 0 until passwords.length()) {
      if (passwords.getJSONObject(i).getString(JSON_SERVICE_ID) == serviceInfo.id) {
        passwords.remove(i)
        break
      }
    }
    val passwordsText = passwords.toString()
    val encryptedText = encryptText(masterPassword, passwordsText)
    EncryptedFileUtils.saveTextToFile(context, PASSWORDS_FILENAME, encryptedText)
  }
}