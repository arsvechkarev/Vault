package com.arsvechkarev.vault.features.services_list.presentation

import com.arsvechkarev.vault.core.model.ServiceModel
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ServicesListView : MvpView {
  
  fun showSettingsIcon()
  
  fun showLoading()
  
  fun showNoServices()
  
  fun showServicesList(list: List<ServiceModel>)
  
  fun showDeleteDialog(serviceModel: ServiceModel)
  
  fun showLoadingDeletingService()
  
  fun showDeletedService(serviceModel: ServiceModel)
}