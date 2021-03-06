package com.arsvechkarev.vault.features.common

import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.model.Service
import com.arsvechkarev.vault.cryptography.ServicesStorage
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServicesRepository @Inject constructor(
  private val storage: ServicesStorage,
  private val threader: Threader
) {
  
  private var servicesList: MutableList<Service> = ArrayList()
  private var changeListeners = ArrayList<((list: List<Service>) -> Unit)>()
  
  fun addChangeListener(listener: (list: List<Service>) -> Unit) {
    changeListeners.add(listener)
  }
  
  fun removeChangeListener(listener: (list: List<Service>) -> Unit) {
    changeListeners.remove(listener)
  }
  
  fun getServices(password: String): List<Service> {
    if (servicesList.isEmpty()) {
      servicesList = storage.getServices(password).toMutableList()
      sortList()
    }
    return servicesList
  }
  
  fun saveService(password: String, service: Service) {
    storage.saveService(password, service)
    servicesList.add(service)
    sortList()
    notifyListeners()
  }
  
  fun updateService(password: String, service: Service) {
    storage.updateService(password, service)
    for (i in 0 until servicesList.size) {
      val currentServiceInfo = servicesList[i]
      if (currentServiceInfo.id == service.id) {
        servicesList[i] = service
        break
      }
    }
    sortList()
    notifyListeners()
  }
  
  fun deleteService(password: String, service: Service, notifyListeners: Boolean) {
    storage.deleteService(password, service)
    servicesList.remove(service)
    if (notifyListeners) {
      notifyListeners()
    }
  }
  
  private fun notifyListeners() {
    threader.onMainThread {
      changeListeners.forEach { it.invoke(servicesList) }
    }
  }
  
  private fun sortList() {
    servicesList.sortWith(Comparator { o1, o2 ->
      return@Comparator o1.serviceName.toLowerCase(Locale.getDefault())
          .compareTo(o2.serviceName.toLowerCase(Locale.getDefault()))
    })
  }
}