package com.arsvechkarev.vault.features.common

import buisnesslogic.ServicesStorage
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.model.ServiceModel
import com.arsvechkarev.vault.core.model.toServiceEntity
import com.arsvechkarev.vault.core.model.toServiceModelList
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServicesRepository @Inject constructor(
  private val storage: ServicesStorage,
  private val threader: Threader
) {
  
  private var servicesList: MutableList<ServiceModel> = ArrayList()
  private var changeListeners = ArrayList<((list: List<ServiceModel>) -> Unit)>()
  
  fun addChangeListener(listener: (list: List<ServiceModel>) -> Unit) {
    changeListeners.add(listener)
  }
  
  fun removeChangeListener(listener: (list: List<ServiceModel>) -> Unit) {
    changeListeners.remove(listener)
  }
  
  fun getServices(password: String): List<ServiceModel> {
    if (servicesList.isEmpty()) {
      servicesList = storage.getServices(password).toServiceModelList().toMutableList()
      sortList()
    }
    return servicesList
  }
  
  fun saveService(password: String, serviceModel: ServiceModel) {
    storage.saveService(password, serviceModel.toServiceEntity())
    servicesList.add(serviceModel)
    sortList()
    notifyListeners()
  }
  
  fun updateService(password: String, serviceModel: ServiceModel) {
    storage.updateService(password, serviceModel.toServiceEntity())
    for (i in 0 until servicesList.size) {
      val currentServiceInfo = servicesList[i]
      if (currentServiceInfo.id == serviceModel.id) {
        servicesList[i] = serviceModel
        break
      }
    }
    sortList()
    notifyListeners()
  }
  
  fun deleteService(password: String, serviceModel: ServiceModel, notifyListeners: Boolean) {
    storage.deleteService(password, serviceModel.toServiceEntity())
    servicesList.remove(serviceModel)
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