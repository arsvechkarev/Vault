package com.arsvechkarev.vault.features.common

import buisnesslogic.ServicesStorage
import com.arsvechkarev.vault.core.model.ServiceModel
import com.arsvechkarev.vault.core.model.toServiceEntity
import com.arsvechkarev.vault.core.model.toServiceModelList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServicesListenableRepository @Inject constructor(private val storage: ServicesStorage) {
  
  private var servicesList: MutableList<ServiceModel> = ArrayList()
  
  private val _servicesFlow = MutableSharedFlow<List<ServiceModel>>()
  val servicesFlow: SharedFlow<List<ServiceModel>> get() = _servicesFlow
  
  suspend fun getServices(password: String): List<ServiceModel> {
    if (servicesList.isEmpty()) {
      servicesList = storage.getServices(password).toServiceModelList().toMutableList()
      sortList()
    }
    return ArrayList(servicesList)
  }
  
  suspend fun saveService(password: String, serviceModel: ServiceModel) {
    storage.saveService(password, serviceModel.toServiceEntity())
    servicesList.add(serviceModel)
    sortList()
    notifySubscribers()
  }
  
  suspend fun updateService(password: String, serviceModel: ServiceModel) {
    storage.updateService(password, serviceModel.toServiceEntity())
    for (i in 0 until servicesList.size) {
      val currentServiceInfo = servicesList[i]
      if (currentServiceInfo.id == serviceModel.id) {
        servicesList[i] = serviceModel
        break
      }
    }
    sortList()
    notifySubscribers()
  }
  
  suspend fun deleteService(password: String, serviceModel: ServiceModel, notifySubscribers: Boolean) {
    storage.deleteService(password, serviceModel.toServiceEntity())
    servicesList.remove(serviceModel)
    if (notifySubscribers) {
      notifySubscribers()
    }
  }
  
  private suspend fun notifySubscribers() {
    _servicesFlow.emit(ArrayList(servicesList))
  }
  
  private fun sortList() {
    servicesList.sortWith(Comparator { o1, o2 ->
      return@Comparator o1.serviceName.toLowerCase(Locale.getDefault())
          .compareTo(o2.serviceName.toLowerCase(Locale.getDefault()))
    })
  }
}