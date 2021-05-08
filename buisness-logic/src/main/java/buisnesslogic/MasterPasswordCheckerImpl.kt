package buisnesslogic

class MasterPasswordCheckerImpl(
  private val cryptography: Cryptography,
  private val fileSaver: FileSaver
) : MasterPasswordChecker {
  
  override fun initializeEncryptedFile(masterPassword: String) {
    val encodedText = cryptography.encryptForTheFirstTime(masterPassword, "")
    fileSaver.saveTextToFile(encodedText)
  }
  
  override fun isCorrect(masterPassword: String): Boolean {
    val textFromFile = fileSaver.readTextFromFile()
    return try {
      val metaInfo = cryptography.getEncryptionMetaInfo(masterPassword, textFromFile)
      cryptography.decryptCipher(masterPassword, metaInfo, textFromFile)
      true
    } catch (e: Throwable) {
      false
    }
  }
}