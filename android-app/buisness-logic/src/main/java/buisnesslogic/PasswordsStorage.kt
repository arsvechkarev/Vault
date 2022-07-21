package buisnesslogic

import buisnesslogic.model.PasswordInfo

/**
 * Storage that provides basic CRUD operations for [PasswordInfo]
 */
interface PasswordsStorage {
  
  /**
   * Returns list of all saved passwords
   *
   * @param masterPassword Master password for encryption/decryption
   */
  suspend fun getPasswords(masterPassword: String): List<PasswordInfo>
  
  /**
   * Saves new password entity
   *
   * @param masterPassword Master password for encryption/decryption
   */
  suspend fun savePassword(masterPassword: String, passwordInfo: PasswordInfo)
  
  /**
   * Updates existing password entity. [passwordInfo] should have the same id as the item that we
   * want to update
   *
   * @param masterPassword Master password for encryption/decryption
   */
  suspend fun updatePasswordInfo(masterPassword: String, passwordInfo: PasswordInfo)
  
  /**
   * Deletes existing password entity. [passwordInfo] should have the same id as the item that we
   * want to delete
   *
   * @param masterPassword Master password for encryption/decryption
   */
  suspend fun deletePassword(masterPassword: String, passwordInfo: PasswordInfo)
}