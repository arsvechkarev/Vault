package com.arsvechkarev.vault.features.creating_master_password

import buisnesslogic.MasterPasswordChecker
import buisnesslogic.MasterPasswordHolder
import buisnesslogic.PasswordChecker
import buisnesslogic.PasswordStatus.OK
import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.UserAuthSaver
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreenState.DIALOG_PASSWORD_STRENGTH
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreenState.ENTERING_PASSWORD
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreenState.REPEATING_PASSWORD
import navigation.Router
import javax.inject.Inject

@FeatureScope
class CreatingMasterPasswordPresenter @Inject constructor(
  private val passwordChecker: PasswordChecker,
  private val masterPasswordChecker: MasterPasswordChecker,
  private val userAuthSaver: UserAuthSaver,
  private val router: Router,
  threader: Threader
) : BasePresenter<CreatingMasterPasswordView>(threader) {
  
  private var state = ENTERING_PASSWORD
  private var previouslyEnteredPassword: String = ""
  
  fun computePasswordStrength(password: String) {
    val strength = passwordChecker.checkStrength(password)
    viewState.showPasswordStrength(strength)
  }
  
  fun onShowPasswordStrengthDialog() {
    viewState.showPasswordStrengthDialog()
    state = DIALOG_PASSWORD_STRENGTH
  }
  
  fun onHidePasswordStrengthDialog() {
    viewState.hidePasswordStrengthDialog()
    state = ENTERING_PASSWORD
  }
  
  fun handleBackPress(): Boolean {
    return when (state) {
      ENTERING_PASSWORD -> false
      DIALOG_PASSWORD_STRENGTH -> {
        viewState.hidePasswordStrengthDialog()
        state = ENTERING_PASSWORD
        true
      }
      REPEATING_PASSWORD -> {
        viewState.switchToEnterPasswordState()
        state = ENTERING_PASSWORD
        true
      }
    }
  }
  
  fun onBackButtonClick() {
    if (state == REPEATING_PASSWORD) {
      state = ENTERING_PASSWORD
      viewState.switchToEnterPasswordState()
    }
  }
  
  fun onEnteredPassword(password: String) {
    require(state == ENTERING_PASSWORD)
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
    require(state == REPEATING_PASSWORD)
    if (previouslyEnteredPassword != "" && password == previouslyEnteredPassword) {
      finishAuthorization()
    } else {
      viewState.showPasswordsDontMatch()
    }
  }
  
  private fun finishAuthorization() {
    viewState.showFinishingAuthorization()
    onBackgroundThread {
      userAuthSaver.setUserIsAuthorized(true)
      masterPasswordChecker.initializeEncryptedFile(previouslyEnteredPassword)
      MasterPasswordHolder.setMasterPassword(previouslyEnteredPassword)
      onMainThread { router.switchToNewRoot(Screens.ServicesListScreen) }
    }
  }
}