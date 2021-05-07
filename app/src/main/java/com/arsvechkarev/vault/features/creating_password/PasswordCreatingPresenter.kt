package com.arsvechkarev.vault.features.creating_password

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.DEFAULT_PASSWORD_LENGTH
import com.arsvechkarev.vault.core.MIN_PASSWORD_LENGTH
import com.arsvechkarev.vault.core.Threader
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
import javax.inject.Inject

@FeatureScope
class PasswordCreatingPresenter @Inject constructor(
  private val passwordChecker: PasswordChecker,
  private val passwordGenerator: PasswordGenerator,
  threader: Threader
) : BasePresenter<PasswordCreatingView>(threader) {
  
  private var passwordCharacteristics = HashSet<PasswordCharacteristics>()
  private var passwordLength = DEFAULT_PASSWORD_LENGTH
  private var password = ""
  
  fun showInitialGeneratedPassword() {
    passwordLength = DEFAULT_PASSWORD_LENGTH
    passwordCharacteristics.clear()
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
      else -> viewState.showSavePasswordClicked(password)
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
  
  }
}