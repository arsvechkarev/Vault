package buisnesslogic

/**
 * Helps checking whether master password is correct or not
 *
 * @see MasterPasswordCheckerImpl
 */
interface MasterPasswordChecker {
  
  /**
   * Initializes encryption file with [masterPassword]
   */
  suspend fun initializeEncryptedFile(masterPassword: Password)
  
  /**
   * Checks whether [masterPassword] is correct or not
   */
  suspend fun isCorrect(masterPassword: Password): Boolean
}