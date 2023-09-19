package buisnesslogic

/**
 * Helps in checking password errors and strength
 */
interface PasswordInfoChecker {
  
  /** Returns [PasswordStrength] or null, if [password] is blank or empty */
  fun checkStrength(password: Password): PasswordStrength?
  
  /** Returns [PasswordError] error, or null, if [password] is fine */
  fun checkForErrors(password: Password): PasswordError?
}