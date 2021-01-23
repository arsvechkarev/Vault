package com.arsvechkarev.vault.features.list.presentation

import com.arsvechkarev.vault.core.model.PasswordInfo
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface PasswordsListView : MvpView {
  
  fun showLoading()
  
  fun showNoPasswords()
  
  fun showPasswordsList(list: List<PasswordInfo>)
}