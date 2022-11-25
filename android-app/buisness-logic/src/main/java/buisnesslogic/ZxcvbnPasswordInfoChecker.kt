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
  
  override fun checkStrength(password: String): PasswordStrength? {
    if (password.isBlank()) return null
    return when (zxcvbn.measure(password).score) {
      2 -> PasswordStrength.MEDIUM
      3 -> PasswordStrength.STRONG
      in 4..Int.MAX_VALUE -> PasswordStrength.VERY_STRONG
      else -> PasswordStrength.WEAK
    }
  }
  
  override fun checkForErrors(password: String): PasswordError? {
    if (password.isBlank()) {
      return PasswordError.EMPTY
    }
    if (password.length < MIN_PASSWORD_LENGTH) {
      return PasswordError.TOO_SHORT
    }
    if (checkStrength(password) == PasswordStrength.WEAK) {
      return PasswordError.TOO_WEAK
    }
    return null
  }
}
