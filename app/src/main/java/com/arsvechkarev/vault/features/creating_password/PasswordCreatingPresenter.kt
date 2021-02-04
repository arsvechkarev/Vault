package com.arsvechkarev.vault.features.creating_password

import com.arsvechkarev.vault.core.DEFAULT_PASSWORD_LENGTH
import com.arsvechkarev.vault.core.MIN_PASSWORD_LENGTH
import com.arsvechkarev.vault.core.extensions.hasNumbers
import com.arsvechkarev.vault.core.extensions.hasSpecialSymbols
import com.arsvechkarev.vault.core.extensions.hasUppercaseLetters
import com.arsvechkarev.vault.core.model.PasswordCharacteristics
import com.arsvechkarev.vault.core.model.PasswordCharacteristics.NUMBERS
import com.arsvechkarev.vault.core.model.PasswordCharacteristics.SPECIAL_SYMBOLS
import com.arsvechkarev.vault.core.model.PasswordCharacteristics.UPPERCASE_SYMBOLS
import com.arsvechkarev.vault.cryptography.PasswordChecker
import com.arsvechkarev.vault.cryptography.PasswordStatus.EMPTY
import com.arsvechkarev.vault.cryptography.PasswordStatus.OK
import com.arsvechkarev.vault.cryptography.PasswordStatus.TOO_SHORT
import com.arsvechkarev.vault.cryptography.PasswordStatus.TOO_WEAK
import com.arsvechkarev.vault.cryptography.generator.PasswordGenerator

class PasswordCreatingPresenter(
  private val passwordChecker: PasswordChecker,
  private val passwordGenerator: PasswordGenerator
) {
  
  private var view: PasswordCreatingView? = null
  
  private var passwordCharacteristics = HashSet<PasswordCharacteristics>()
  private var passwordLength = DEFAULT_PASSWORD_LENGTH
  private var password = ""
  
  fun attachView(view: PasswordCreatingView) {
    this.view = view
  }
  
  fun showInitialGeneratedPassword() {
    onGeneratePasswordClicked()
  }
  
  fun onGeneratePasswordClicked() {
    password = passwordGenerator.generatePassword(passwordLength, passwordCharacteristics)
    showPasswordInfo()
    view?.showGeneratedPassword(password)
  }
  
  fun onPasswordChanged(password: String) {
    this.password = password
    showPasswordInfo()
  }
  
  fun onPasswordLengthChanged(seekBarProgress: Int) {
    val length = seekBarProgress + MIN_PASSWORD_LENGTH
    passwordLength = length
    view?.showChangePasswordLength(length)
  }
  
  fun onCheckmarkClicked(characteristics: PasswordCharacteristics) {
    if (passwordCharacteristics.contains(characteristics)) {
      passwordCharacteristics.remove(characteristics)
    } else {
      passwordCharacteristics.add(characteristics)
    }
    view?.showPasswordCharacteristics(passwordCharacteristics)
  }
  
  fun onSavePasswordClicked() {
    when (passwordChecker.validate(password)) {
      EMPTY -> view?.showPasswordIsEmpty()
      TOO_SHORT -> view?.showPasswordIsTooShort()
      TOO_WEAK -> view?.showPasswordIsTooWeak()
      OK -> view?.showSavePasswordClicked(password)
    }
  }
  
  fun detachView() {
    view = null
  }
  
  private fun showPasswordInfo() {
    fillPasswordCharacteristics()
    view?.showPasswordCharacteristics(passwordCharacteristics)
    view?.showPasswordStrength(passwordChecker.checkStrength(password))
  }
  
  private fun fillPasswordCharacteristics() {
    passwordCharacteristics.clear()
    if (password.hasUppercaseLetters) passwordCharacteristics.add(UPPERCASE_SYMBOLS)
    if (password.hasNumbers) passwordCharacteristics.add(NUMBERS)
    if (password.hasSpecialSymbols) passwordCharacteristics.add(SPECIAL_SYMBOLS)
  }
}