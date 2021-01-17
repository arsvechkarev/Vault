package com.arsvechkarev.vault.password

import org.json.JSONArray

interface PasswordsSaver {
  
  fun getDecryptedPasswordsList(masterPassword: String): JSONArray
  
  fun savePassword(masterPassword: String, serviceName: String, servicePassword: String)
  
  fun deletePassword(masterPassword: String, serviceName: String)
}