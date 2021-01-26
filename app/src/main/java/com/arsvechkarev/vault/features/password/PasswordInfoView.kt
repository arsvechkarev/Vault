package com.arsvechkarev.vault.features.password

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface PasswordInfoView : MvpView {
  
  fun showRegeneratedPassword(password: String)
}