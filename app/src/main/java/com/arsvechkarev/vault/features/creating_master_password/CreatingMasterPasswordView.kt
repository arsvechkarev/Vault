package com.arsvechkarev.vault.features.creating_master_password

import buisnesslogic.PasswordStatus
import buisnesslogic.PasswordStrength
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CreatingMasterPasswordView : MvpView {
  
  fun showPasswordProblem(passwordStatus: PasswordStatus)
  
  fun showPasswordStrength(strength: PasswordStrength?)
  
  fun showPasswordStrengthDialog()
  
  fun hidePasswordStrengthDialog()
  
  fun switchToEnterPasswordState()
  
  fun switchToRepeatPasswordState()
  
  fun showPasswordsDontMatch()
  
  fun showFinishingAuthorization()
}