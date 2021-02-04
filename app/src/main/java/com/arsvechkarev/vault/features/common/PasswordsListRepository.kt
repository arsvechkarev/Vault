package com.arsvechkarev.vault.features.common

import com.arsvechkarev.vault.core.JSON_SERVICE_EMAIL
import com.arsvechkarev.vault.core.JSON_SERVICE_ID
import com.arsvechkarev.vault.core.JSON_SERVICE_NAME
import com.arsvechkarev.vault.core.JSON_SERVICE_PASSWORD
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.extensions.transformToArrayList
import com.arsvechkarev.vault.core.model.ServiceInfo
import com.arsvechkarev.vault.cryptography.PasswordsStorage

class PasswordsListRepository(
  private val storage: PasswordsStorage,
  private val threader: Threader
) {
  
  private var servicesList = ArrayList<ServiceInfo>()
  private var changeListeners = ArrayList<((list: List<ServiceInfo>) -> Unit)>()
  
  fun addChangeListener(listener: (list: List<ServiceInfo>) -> Unit) {
    changeListeners.add(listener)
  }
  
  fun removeChangeListener(listener: (list: List<ServiceInfo>) -> Unit) {
    changeListeners.remove(listener)
  }
  
  fun getAllServicesInfo(masterPassword: String): List<ServiceInfo> {
    if (servicesList.isEmpty()) {
      servicesList = storage.getServicesInfoList(masterPassword)
          .transformToArrayList { jsonObject ->
            ServiceInfo(
              jsonObject.getString(JSON_SERVICE_ID),
              jsonObject.getString(JSON_SERVICE_NAME),
              jsonObject.getString(JSON_SERVICE_EMAIL),
              jsonObject.getString(JSON_SERVICE_PASSWORD),
            )
          }
      servicesList.sortBy { it.name }
    }
    return servicesList
  }
  
  fun saveServiceInfo(masterPassword: String, serviceInfo: ServiceInfo) {
    storage.saveServiceInfo(masterPassword, serviceInfo)
    servicesList.add(serviceInfo)
    notifyListeners()
  }
  
  fun updateServiceInfo(masterPassword: String, serviceInfo: ServiceInfo) {
    storage.updateServiceInfo(masterPassword, serviceInfo)
    for (i in 0 until servicesList.size) {
      val currentServiceInfo = servicesList[i]
      if (currentServiceInfo.id == serviceInfo.id) {
        servicesList[i] = serviceInfo
        break
      }
    }
    notifyListeners()
  }
  
  fun deleteServiceInfo(masterPassword: String, serviceInfo: ServiceInfo) {
    storage.deleteServiceInfo(masterPassword, serviceInfo)
    servicesList.remove(serviceInfo)
    notifyListeners()
  }
  
  private fun notifyListeners() {
    servicesList.sortBy { it.name }
    threader.onMainThread {
      changeListeners.forEach { it.invoke(servicesList) }
    }
  }
}