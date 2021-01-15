package com.arsvechkarev.vault.features.start

import com.arsvechkarev.vault.core.password.PasswordStrength
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface StartView : MvpView {
  
  fun showLoading()
  
  fun showPasswordIsWrong()
  
  fun showPasswordStrength(strength: PasswordStrength?)
}