package com.arsvechkarev.vault.features.services_list

import buisnesslogic.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.core.model.ServiceModel
import com.arsvechkarev.vault.features.common.ServicesRepository
import navigation.Router
import javax.inject.Inject

@FeatureScope
class ServicesListPresenter @Inject constructor(
  private val servicesRepository: ServicesRepository,
  private val router: Router,
  threader: Threader
) : BasePresenter<ServicesListView>(threader) {
  
  private val listChangeListener: (List<ServiceModel>) -> Unit = { list ->
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
    onIoThread {
      servicesRepository.deleteService(masterPassword, serviceModel, notifyListeners = false)
      onMainThread { viewState.showDeletedService(serviceModel) }
    }
  }
}