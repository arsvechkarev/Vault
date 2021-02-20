package com.arsvechkarev.vault.features.services_list

import com.arsvechkarev.vault.core.model.Service
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ServicesListView : MvpView {
  
  fun showLoading()
  
  fun showNoPasswords()
  
  fun showPasswordsList(list: List<Service>)
  
  fun showDeleteDialog(service: Service)
  
  fun showLoadingDeletingService()
  
  fun showDeletedService(service: Service)
}