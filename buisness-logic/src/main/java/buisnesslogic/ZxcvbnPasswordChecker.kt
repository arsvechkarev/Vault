package buisnesslogic

import com.nulabinc.zxcvbn.Zxcvbn

class ZxcvbnPasswordChecker(
  private val zxcvbn: Zxcvbn
) : PasswordChecker {
  
  override fun checkStrength(password: String): PasswordStrength? {
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
  
  override fun validate(password: String): PasswordStatus {
    if (password.isBlank()) {
      return PasswordStatus.EMPTY
    }
    if (password.length < MIN_PASSWORD_LENGTH) {
      return PasswordStatus.TOO_SHORT
    }
    if (checkStrength(password) == PasswordStrength.WEAK) {
      return PasswordStatus.TOO_WEAK
    }
    return PasswordStatus.OK
  }
}