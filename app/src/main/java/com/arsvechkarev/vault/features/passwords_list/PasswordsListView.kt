package com.arsvechkarev.vault.features.passwords_list

import com.arsvechkarev.vault.core.model.ServiceInfo
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface PasswordsListView : MvpView {
  
  fun showLoading()
  
  fun showNoPasswords()
  
  fun showPasswordsList(list: List<ServiceInfo>)
  
  fun showEnterServiceNameDialog()
}