package com.arsvechkarev.vault.core.password

import com.arsvechkarev.vault.core.extensions.hasLowercaseLetters
import com.arsvechkarev.vault.core.extensions.hasNumbers
import com.arsvechkarev.vault.core.extensions.hasSpecialSymbols
import com.arsvechkarev.vault.core.extensions.hasUppercaseLetters
import com.nulabinc.zxcvbn.Zxcvbn

class ZxcvbnPasswordChecker(
  private val zxcvbn: Zxcvbn
) : PasswordChecker {
  
  override fun check(password: String): PasswordStrength? {
    if (password.isBlank()) return null
    var strengthNum = zxcvbn.measure(password).score
    if (password.hasNumbers) strengthNum += 1
    if (password.hasUppercaseLetters) strengthNum += 1
    if (password.hasLowercaseLetters) strengthNum += 1
    if (password.hasSpecialSymbols) strengthNum += 1
    return when (strengthNum) {
      in 4..5 -> PasswordStrength.MEDIUM
      in 6..7 -> PasswordStrength.STRONG
      8 -> PasswordStrength.VERY_STRONG
      else -> PasswordStrength.WEAK
    }
  }
}