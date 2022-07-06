package buisnesslogic

/**
 * Checker that helps validating password and checking it strength
 */
interface PasswordChecker {
  
  fun checkStrength(password: String): PasswordStrength?
  
  fun getPasswordStatus(password: String): PasswordStatus
}