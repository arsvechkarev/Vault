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
  fun initializeEncryptedFile(masterPassword: String)
  
  /**
   * Checks whether [masterPassword] is correct or not
   */
  fun isCorrect(masterPassword: String): Boolean
}