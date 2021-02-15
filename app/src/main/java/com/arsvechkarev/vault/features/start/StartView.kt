package com.arsvechkarev.vault.features.start

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface StartView : MvpView {
  
  fun showLoading()
  
  fun showError()
  
  fun showSuccess()
}