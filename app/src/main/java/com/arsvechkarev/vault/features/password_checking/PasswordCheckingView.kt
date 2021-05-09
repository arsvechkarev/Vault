package com.arsvechkarev.vault.features.password_checking

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface PasswordCheckingView : MvpView {
  
  fun showDialog()
  
  fun hideDialog()
  
  fun showPasswordCheckingLoading()
  
  fun showPasswordCheckingFinished()
  
  fun showPasswordIsIncorrect()
}