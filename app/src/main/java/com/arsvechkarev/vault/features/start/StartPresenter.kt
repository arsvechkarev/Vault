package com.arsvechkarev.vault.features.start

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.password.PasswordChecker

class StartPresenter(
  threader: Threader,
  private val passwordChecker: PasswordChecker
) : BasePresenter<StartView>(threader) {
  
  fun computePasswordStrength(password: String) {
    val strength = passwordChecker.check(password)
    viewState.showPasswordStrength(strength)
  }
}