package com.arsvechkarev.vault.features.start

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface StartView : MvpView {
  
  fun showKeyboard()
  
  fun showLoadingCheckingPassword()
  
  fun showFailureCheckingPassword()
  
  fun showSuccessCheckingPassword()
}