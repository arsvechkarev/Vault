package buisnesslogic

interface Cryptography {
  
  /**
   * Performs initial encryption of [data] with [password] and returns encrypted text. After that
   * returns result can be decrypted with the [password] and [data] can be retrieved
   */
  fun encryptForTheFirstTime(password: String, data: String): String
  
  /**
   * Encrypts [plaintext] with [password] and returns encrypted data. In addition to [plaintext]
   * and [password] you should provide [ciphertext] that contains meta information about encryption,
   * such as salt, iv, etc. In order to get ciphertext, you should call [encryptForTheFirstTime] first
   *
   * @see encryptForTheFirstTime
   * @see decryptData
   */
  fun encryptData(password: String, plaintext: String, ciphertext: String): String
  
  /**
   * Decrypts [ciphertext] using given [password] and returns decrypted plaintext. If decryption
   * was unsuccessful, throws exception
   */
  fun decryptData(password: String, ciphertext: String): String
}