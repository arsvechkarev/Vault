package com.arsvechkarev.vault.cryptography

import com.arsvechkarev.vault.core.model.ServiceInfo
import org.json.JSONArray

interface PasswordsStorage {
  
  fun getServicesInfoList(masterPassword: String): JSONArray
  
  fun saveServiceInfo(masterPassword: String, serviceInfo: ServiceInfo)
  
  fun updateServiceInfo(masterPassword: String, serviceInfo: ServiceInfo)
  
  fun deleteServiceInfo(masterPassword: String, serviceInfo: ServiceInfo)
}