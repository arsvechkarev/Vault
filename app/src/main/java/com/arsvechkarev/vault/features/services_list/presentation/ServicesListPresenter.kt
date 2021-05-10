package com.arsvechkarev.vault.features.services_list.presentation

import buisnesslogic.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Dispatchers
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.core.model.ServiceModel
import com.arsvechkarev.vault.features.common.Screens
import com.arsvechkarev.vault.features.common.ServicesListenableRepository
import com.arsvechkarev.vault.features.common.biometrics.BiometricsAvailabilityChecker
import kotlinx.coroutines.launch
import navigation.Router
import javax.inject.Inject

@FeatureScope
class ServicesListPresenter @Inject constructor(
  private val servicesRepository: ServicesListenableRepository,
  private val biometricsAvailabilityChecker: BiometricsAvailabilityChecker,
  private val router: Router,
  dispatchers: Dispatchers
) : BasePresenter<ServicesListView>(dispatchers) {
  
  override fun onFirstViewAttach() {
    if (biometricsAvailabilityChecker.isBiometricsSupported()) {
      viewState.showSettingsIcon()
    }
    servicesRepository.flow.collectInPresenterScope { list ->
      if (list.isNotEmpty()) {
        viewState.showServicesList(list)
      } else {
        viewState.showNoServices()
      }
    }
    startLoadingPasswords()
  }
  
  fun onSettingsIconClicked() {
    router.goForward(Screens.SettingsScreen)
  }
  
  fun onServiceItemClicked(serviceModel: ServiceModel) {
    router.goForward(Screens.InfoScreen(serviceModel))
  }
  
  fun onNewServiceClicked() {
    router.goForward(Screens.CreatingServiceScreen)
  }
  
  fun onServiceLongItemClicked(serviceModel: ServiceModel) {
    viewState.showDeleteDialog(serviceModel)
  }
  
  fun deleteService(serviceModel: ServiceModel) {
    viewState.showLoadingDeletingService()
    launch {
      onIoThread {
        servicesRepository.deleteService(masterPassword, serviceModel, notifySubscribers = false)
      }
      viewState.showDeletedService(serviceModel)
    }
  }
  
  private fun startLoadingPasswords() {
    launch {
      viewState.showLoading()
      val services = onIoThread { servicesRepository.getServices(masterPassword) }
      if (services.isNotEmpty()) {
        viewState.showServicesList(services)
      } else {
        viewState.showNoServices()
      }
    }
  }
}