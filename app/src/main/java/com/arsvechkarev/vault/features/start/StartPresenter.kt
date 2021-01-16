package com.arsvechkarev.vault.features.start

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.extensions.assertThat
import com.arsvechkarev.vault.core.password.PasswordChecker
import com.arsvechkarev.vault.core.password.PasswordStatus.OK
import com.arsvechkarev.vault.features.start.StartScreenState.ENTERING_PASSWORD
import com.arsvechkarev.vault.features.start.StartScreenState.REPEATING_PASSWORD

class StartPresenter(
  threader: Threader,
  private val passwordChecker: PasswordChecker
) : BasePresenter<StartView>(threader) {
  
  private var state = ENTERING_PASSWORD
  private var previouslyEnteredPassword: String = ""
  
  fun computePasswordStrength(password: String) {
    val strength = passwordChecker.check(password)
    viewState.showPasswordStrength(strength)
  }
  
  fun allowBackPress(): Boolean {
    if (state == REPEATING_PASSWORD) {
      state = ENTERING_PASSWORD
      viewState.switchToEnterPasswordState()
      return false
    }
    return true
  }
  
  fun onBackButtonClick() {
    if (state == REPEATING_PASSWORD) {
      state = ENTERING_PASSWORD
      viewState.switchToEnterPasswordState()
    }
  }
  
  fun onEnteredPassword(password: String) {
    assertThat(state == ENTERING_PASSWORD)
    val passwordStatus = passwordChecker.validate(password)
    if (passwordStatus == OK) {
      state = REPEATING_PASSWORD
      previouslyEnteredPassword = password
      viewState.switchToRepeatPasswordState()
    } else {
      viewState.showPasswordProblem(passwordStatus)
    }
  }
  
  fun onRepeatedPassword(password: String) {
    if (previouslyEnteredPassword != "" && password == previouslyEnteredPassword) {
      viewState.showPasswordRepeatedCorrectly()
    } else {
      viewState.showPasswordsDontMatch()
    }
  }
}