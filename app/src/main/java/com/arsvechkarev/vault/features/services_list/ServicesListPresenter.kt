package com.arsvechkarev.vault.features.services_list

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.model.Service
import com.arsvechkarev.vault.cryptography.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.features.common.ServicesRepository

class ServicesListPresenter(
  threader: Threader,
  private val servicesRepository: ServicesRepository
) : BasePresenter<ServicesListView>(threader) {
  
  private val listChangeListener: (List<Service>) -> Unit = { list ->
    if (list.isNotEmpty()) {
      viewState.showServicesList(list)
    } else {
      viewState.showNoServices()
    }
  }
  
  override fun onFirstViewAttach() {
    servicesRepository.addChangeListener(listChangeListener)
  }
  
  fun startLoadingPasswords() {
    onIoThread {
      updateViewState { showLoading() }
      val services = servicesRepository.getServices(masterPassword)
      if (services.isNotEmpty()) {
        updateViewState { showServicesList(services) }
      } else {
        updateViewState { showNoServices() }
      }
    }
  }
  
  fun onLongClick(service: Service) {
    viewState.showDeleteDialog(service)
  }
  
  fun deleteService(service: Service) {
    viewState.showLoadingDeletingService()
    onIoThread {
      servicesRepository.deleteService(masterPassword, service,
        notifyListeners = false)
      updateViewState { showDeletedService(service) }
    }
  }
}