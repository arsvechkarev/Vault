package com.arsvechkarev.vault.features.creating_password

import buisnesslogic.DEFAULT_PASSWORD_LENGTH
import buisnesslogic.MIN_PASSWORD_LENGTH
import buisnesslogic.PasswordError.EMPTY
import buisnesslogic.PasswordInfoChecker
import buisnesslogic.generator.PasswordGenerator
import buisnesslogic.hasNumbers
import buisnesslogic.hasSpecialSymbols
import buisnesslogic.hasUppercaseLetters
import buisnesslogic.model.PasswordCharacteristics
import buisnesslogic.model.PasswordCharacteristics.NUMBERS
import buisnesslogic.model.PasswordCharacteristics.SPECIAL_SYMBOLS
import buisnesslogic.model.PasswordCharacteristics.UPPERCASE_SYMBOLS
import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.core.Router
import com.arsvechkarev.vault.core.communicators.FlowCommunicator
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ConfigureMode.EditPassword
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ConfigureMode.NewPassword
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ExitScreen
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ShowAcceptPasswordDialog
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ShowLoading
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingReactions.OnNewPasswordAccepted
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingReactions.OnSavePasswordButtonClicked
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingState.INITIAL
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingState.LOADING
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingState.SHOWING_ACCEPT_DIALOG
import kotlinx.coroutines.launch
import java.util.EnumSet

class PasswordCreatingPresenter constructor(
  @PasswordCreatingCommunicator
  private val passwordCreatingCommunicator: FlowCommunicator<PasswordCreatingEvents>,
  private val passwordInfoChecker: PasswordInfoChecker,
  private val passwordGenerator: PasswordGenerator,
  private val router: Router,
  dispatchers: DispatchersFacade
) : BasePresenter<PasswordCreatingView>(dispatchers) {
  
  private var passwordCharacteristics = EnumSet.of(UPPERCASE_SYMBOLS, NUMBERS, SPECIAL_SYMBOLS)
  private var passwordLength = DEFAULT_PASSWORD_LENGTH
  private var password = ""
  private var state = INITIAL
  
  init {
    subscribeToPasswordCreatingEvents()
  }
  
  fun showInitialGeneratedPassword() {
    passwordLength = DEFAULT_PASSWORD_LENGTH
    onGeneratePasswordClicked()
  }
  
  fun onGeneratePasswordClicked() {
    password = passwordGenerator.generatePassword(passwordLength, passwordCharacteristics)
    showPasswordInfo()
    viewState.showGeneratedPassword(password)
  }
  
  fun onPasswordChanged(password: String) {
    this.password = password.trim()
    showPasswordInfo()
  }
  
  fun onPasswordLengthChanged(seekBarProgress: Int) {
    val length = seekBarProgress + MIN_PASSWORD_LENGTH
    passwordLength = length
    viewState.showChangePasswordLength(length)
  }
  
  fun onCheckmarkClicked(characteristics: PasswordCharacteristics) {
    if (passwordCharacteristics.contains(characteristics)) {
      passwordCharacteristics.remove(characteristics)
    } else {
      passwordCharacteristics.add(characteristics)
    }
    viewState.showPasswordCharacteristics(passwordCharacteristics)
  }
  
  fun onSavePasswordClicked() {
    when (passwordInfoChecker.checkForErrors(password)) {
      EMPTY -> viewState.showPasswordIsEmpty()
      else -> launch { passwordCreatingCommunicator.send(OnSavePasswordButtonClicked(password)) }
    }
  }
  
  private fun showPasswordInfo() {
    fillPasswordCharacteristics()
    viewState.showPasswordCharacteristics(passwordCharacteristics)
    viewState.showPasswordStrength(passwordInfoChecker.checkStrength(password))
  }
  
  private fun fillPasswordCharacteristics() {
    passwordCharacteristics.clear()
    if (password.hasUppercaseLetters) passwordCharacteristics.add(UPPERCASE_SYMBOLS)
    if (password.hasNumbers) passwordCharacteristics.add(NUMBERS)
    if (password.hasSpecialSymbols) passwordCharacteristics.add(SPECIAL_SYMBOLS)
  }
  
  fun onCloseClicked() {
    if (!handleBackPress()) router.goBack(releaseCurrentScreen = false)
  }
  
  fun handleBackPress(): Boolean {
    return when (state) {
      INITIAL -> false
      SHOWING_ACCEPT_DIALOG -> {
        viewState.hidePasswordAcceptingDialog()
        true
      }
      LOADING -> true
    }
  }
  
  fun onHideAcceptPasswordDialog() {
    state = INITIAL
    viewState.hidePasswordAcceptingDialog()
  }
  
  fun acceptPassword() {
    launch { passwordCreatingCommunicator.send(OnNewPasswordAccepted(password)) }
  }
  
  private fun subscribeToPasswordCreatingEvents() {
    passwordCreatingCommunicator.events.collectInPresenterScope { event ->
      when (event) {
        NewPassword -> {
          viewState.showCreatingPasswordMode()
          showInitialGeneratedPassword()
        }
        is EditPassword -> {
          this.password = event.password
          viewState.showEditingPasswordMode(password)
          onPasswordChanged(password)
        }
        ShowAcceptPasswordDialog -> {
          state = SHOWING_ACCEPT_DIALOG
          viewState.showPasswordAcceptingDialog()
        }
        ShowLoading -> {
          state = LOADING
          viewState.hidePasswordAcceptingDialog()
          viewState.showLoadingDialog()
        }
        ExitScreen -> {
          state = INITIAL
          viewState.hidePasswordAcceptingDialog()
          viewState.hideLoadingDialog()
        }
      }
    }
  }
}