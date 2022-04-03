package buisnesslogic

/**
 * Checker that helps validating password and checking it strength
 */
interface PasswordChecker {

    fun checkStrength(password: String): PasswordStrength?

    fun validate(password: String): PasswordStatus
}