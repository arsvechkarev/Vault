package com.arsvechkarev.vault.features.initial_screen

import com.arsvechkarev.vault.cryptography.PasswordStatus
import com.arsvechkarev.vault.cryptography.PasswordStrength
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface StartView : MvpView {
  
  fun showPasswordProblem(passwordStatus: PasswordStatus)
  
  fun showPasswordStrength(strength: PasswordStrength?)
  
  fun switchToEnterPasswordState()
  
  fun switchToRepeatPasswordState()
  
  fun showPasswordRepeatedCorrectly()
  
  fun showPasswordsDontMatch()
  
  fun showFinishingAuthorization()
  
  fun goToPasswordsList()
}