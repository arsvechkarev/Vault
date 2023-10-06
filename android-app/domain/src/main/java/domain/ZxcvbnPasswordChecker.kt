package domain

import com.nulabinc.zxcvbn.Zxcvbn

/**
 * Password checker that uses zxcvbn library to check password strength.
 *
 * Library could be found at https://github.com/dropbox/zxcvbn
 */
class ZxcvbnPasswordChecker(
  private val zxcvbn: Zxcvbn
) : PasswordChecker {
  
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
  
  override fun checkStatus(password: Password): PasswordStatus {
    val passwordString = password.stringData
    if (passwordString.isBlank()) {
      return PasswordStatus.EMPTY
    }
    if (checkStrength(password) == PasswordStrength.WEAK) {
      return PasswordStatus.TOO_WEAK
    }
    return PasswordStatus.OK
  }
}
