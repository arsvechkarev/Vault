package com.arsvechkarev.vault.features.services_list

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.core.model.Service
import com.arsvechkarev.vault.cryptography.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.features.common.ServicesRepository
import navigation.Router
import javax.inject.Inject

@FeatureScope
class ServicesListPresenter @Inject constructor(
  private val servicesRepository: ServicesRepository,
  private val router: Router,
  threader: Threader
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
    startLoadingPasswords()
  }
  
  private fun startLoadingPasswords() {
    onIoThread {
      onMainThread { viewState.showLoading() }
      val services = servicesRepository.getServices(masterPassword)
      if (services.isNotEmpty()) {
        onMainThread { viewState.showServicesList(services) }
      } else {
        onMainThread { viewState.showNoServices() }
      }
    }
  }
  
  fun onServiceItemClicked(service: Service) {
    router.goForward(Screens.InfoScreen(service))
  }
  
  fun onNewServiceClicked() {
    router.goForward(Screens.CreateServiceScreen)
  }
  
  fun onServiceLongItemClicked(service: Service) {
    viewState.showDeleteDialog(service)
  }
  
  fun deleteService(service: Service) {
    viewState.showLoadingDeletingService()
    onIoThread {
      servicesRepository.deleteService(masterPassword, service, notifyListeners = false)
      onMainThread { viewState.showDeletedService(service) }
    }
  }
}