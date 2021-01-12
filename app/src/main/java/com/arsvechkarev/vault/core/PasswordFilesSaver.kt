package com.arsvechkarev.vault.core

import org.json.JSONArray

interface PasswordFilesSaver {
  
  fun getDecryptedPasswordsList(masterPassword: String): JSONArray
  
  fun savePassword(masterPassword: String, serviceName: String, servicePassword: String)
  
  fun deletePassword(masterPassword: String, serviceName: String)
}