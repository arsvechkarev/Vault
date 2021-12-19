package buisnesslogic

class MasterPasswordCheckerImpl(
  private val cryptography: Cryptography,
  private val fileSaver: FileSaver
) : MasterPasswordChecker {
  
  override fun initializeEncryptedFile(masterPassword: String) {
    val encodedText = cryptography.encryptData(masterPassword, "")
    fileSaver.saveData(encodedText)
  }
  
  override fun isCorrect(masterPassword: String): Boolean {
    val ciphertext = fileSaver.readData() ?: return false
    return try {
      cryptography.decryptData(masterPassword, ciphertext)
      // Decryption was successful, returning true
      true
    } catch (e: Throwable) {
      // Error happened during decryption, returning false
      false
    }
  }
}