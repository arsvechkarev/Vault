package buisnesslogic

/**
 * Helps checking password status and strength
 */
interface PasswordInfoChecker {
  
  /** Returns [PasswordStrength] or null, if [password] is blank or empty */
  fun checkStrength(password: Password): PasswordStrength?
  
  fun checkStatus(password: Password): PasswordStatus
}