package com.arsvechkarev.vault.features.creating_password

import com.arsvechkarev.vault.core.BasePresenterWithChannels
import com.arsvechkarev.vault.core.DEFAULT_PASSWORD_LENGTH
import com.arsvechkarev.vault.core.MIN_PASSWORD_LENGTH
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.channels.Channel
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.core.extensions.hasNumbers
import com.arsvechkarev.vault.core.extensions.hasSpecialSymbols
import com.arsvechkarev.vault.core.extensions.hasUppercaseLetters
import com.arsvechkarev.vault.core.model.PasswordCharacteristics
import com.arsvechkarev.vault.core.model.PasswordCharacteristics.NUMBERS
import com.arsvechkarev.vault.core.model.PasswordCharacteristics.SPECIAL_SYMBOLS
import com.arsvechkarev.vault.core.model.PasswordCharacteristics.UPPERCASE_SYMBOLS
import com.arsvechkarev.vault.cryptography.PasswordChecker
import com.arsvechkarev.vault.cryptography.PasswordStatus.EMPTY
import com.arsvechkarev.vault.cryptography.generator.PasswordGenerator
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
import navigation.Router
import javax.inject.Inject
import javax.inject.Named

@FeatureScope
class PasswordCreatingPresenter @Inject constructor(
  private val passwordChecker: PasswordChecker,
  private val passwordGenerator: PasswordGenerator,
  @Named(PasswordCreatingTag) private val passwordCreatingChannel: Channel<PasswordCreatingEvents>,
  private val router: Router,
  threader: Threader
) : BasePresenterWithChannels<PasswordCreatingView>(threader) {
  
  private var passwordCharacteristics = hashSetOf(UPPERCASE_SYMBOLS, NUMBERS, SPECIAL_SYMBOLS)
  private var passwordLength = DEFAULT_PASSWORD_LENGTH
  private var password = ""
  private var state = INITIAL
  
  init {
    subscribeToChannel(passwordCreatingChannel) { event ->
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
    when (passwordChecker.validate(password)) {
      EMPTY -> viewState.showPasswordIsEmpty()
      else -> passwordCreatingChannel.send(OnSavePasswordButtonClicked(password))
    }
  }
  
  private fun showPasswordInfo() {
    fillPasswordCharacteristics()
    viewState.showPasswordCharacteristics(passwordCharacteristics)
    viewState.showPasswordStrength(passwordChecker.checkStrength(password))
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
    passwordCreatingChannel.send(OnNewPasswordAccepted(password))
  }
}