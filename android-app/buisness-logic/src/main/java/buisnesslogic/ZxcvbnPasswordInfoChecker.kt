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
    var strengthNum = zxcvbn.measure(password).score
    // TODO (8/15/2022): Maybe leave only zxcvbn
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