package com.arsvechkarev.vault.features.creating_password

import com.arsvechkarev.vault.core.model.PasswordCharacteristics
import com.arsvechkarev.vault.cryptography.PasswordStrength

interface PasswordCreatingView {
  
  fun showChangePasswordLength(progress: Int)
  
  fun showPasswordStrength(strength: PasswordStrength?)
  
  fun showPasswordCharacteristics(characteristics: Collection<PasswordCharacteristics>)
  
  fun showGeneratedPassword(password: String)
  
  fun showPasswordIsEmpty()
  
  fun showPasswordIsTooWeak()
  
  fun showPasswordIsTooShort()
  
  fun showSavePasswordClicked(password: String)
}