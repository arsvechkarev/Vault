package com.arsvechkarev.vault.features.start

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.UserAuthSaver
import com.arsvechkarev.vault.core.extensions.assertThat
import com.arsvechkarev.vault.features.start.StartScreenState.ENTERING_PASSWORD
import com.arsvechkarev.vault.features.start.StartScreenState.REPEATING_PASSWORD
import com.arsvechkarev.vault.password.MasterPasswordChecker
import com.arsvechkarev.vault.password.MasterPasswordHolder
import com.arsvechkarev.vault.password.MasterPasswordSaver
import com.arsvechkarev.vault.password.PasswordStatus.OK
import com.arsvechkarev.vault.password.PasswordVerifier

class StartPresenter(
  threader: Threader,
  private val passwordVerifier: PasswordVerifier,
  private val masterPasswordSaver: MasterPasswordSaver,
  private val masterPasswordChecker: MasterPasswordChecker,
  private val userAuthSaver: UserAuthSaver
) : BasePresenter<StartView>(threader) {
  
  private var state = ENTERING_PASSWORD
  private var previouslyEnteredPassword: String = ""
  
  fun computePasswordStrength(password: String) {
    val strength = passwordVerifier.check(password)
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
    val passwordStatus = passwordVerifier.validate(password)
    if (passwordStatus == OK) {
      state = REPEATING_PASSWORD
      previouslyEnteredPassword = password
      viewState.switchToRepeatPasswordState()
    } else {
      viewState.showPasswordProblem(passwordStatus)
    }
  }
  
  fun onRepeatedPassword(password: String) {
    assertThat(state == REPEATING_PASSWORD)
    if (previouslyEnteredPassword != "" && password == previouslyEnteredPassword) {
      viewState.showPasswordRepeatedCorrectly()
    } else {
      viewState.showPasswordsDontMatch()
    }
  }
  
  fun savePasswordLocally() {
    assertThat(previouslyEnteredPassword != "")
    masterPasswordSaver.saveMasterPassword(previouslyEnteredPassword)
    finishAuthorization()
  }
  
  fun doNotSavePasswordLocally() {
    finishAuthorization()
  }
  
  private fun finishAuthorization() {
    updateViewState { showFinishingAuthorization() }
    onBackgroundThread {
      userAuthSaver.setUserIsAuthorized(true)
      MasterPasswordHolder.setMasterPassword(previouslyEnteredPassword)
      masterPasswordChecker.encodeSecretPhrase(previouslyEnteredPassword)
      updateViewState { goToPasswordsList() }
    }
  }
}