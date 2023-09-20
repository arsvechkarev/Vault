package buisnesslogic

import com.nulabinc.zxcvbn.Zxcvbn

/**
 * Password checker that uses zxcvbn library to check password strength.
 *
 * Library could be found at https://github.com/dropbox/zxcvbn
 */
class ZxcvbnPasswordInfoChecker(
  private val zxcvbn: Zxcvbn
) : PasswordInfoChecker {
  
  override fun checkStrength(password: Password): PasswordStrength? {
    val passwordString = password.stringData
    if (passwordString.isBlank()) return null
    return when (zxcvbn.measure(passwordString).score) {
      2 -> PasswordStrength.MEDIUM
      3 -> PasswordStrength.STRONG
      in 4..Int.MAX_VALUE -> PasswordStrength.SECURE
      else -> PasswordStrength.WEAK
    }
  }
  
  override fun checkForErrors(password: Password): PasswordError? {
    val passwordString = password.stringData
    if (passwordString.isBlank()) {
      return PasswordError.EMPTY
    }
    if (passwordString.length < MIN_PASSWORD_LENGTH) {
      return PasswordError.TOO_SHORT
    }
    if (checkStrength(password) == PasswordStrength.WEAK) {
      return PasswordError.TOO_WEAK
    }
    return null
  }
}
