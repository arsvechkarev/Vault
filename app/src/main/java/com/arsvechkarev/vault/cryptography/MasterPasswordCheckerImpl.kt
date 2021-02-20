package com.arsvechkarev.vault.cryptography

import android.annotation.SuppressLint
import com.arsvechkarev.vault.core.FileSaver
import com.arsvechkarev.vault.core.PASSWORDS_FILENAME

class MasterPasswordCheckerImpl(
  private val cryptography: Cryptography,
  private val fileSaver: FileSaver
) : MasterPasswordChecker {
  
  @SuppressLint("ApplySharedPref")
  override fun initializeEncryptedFile(masterPassword: String) {
    val encodedText = cryptography.encryptForTheFirstTime(masterPassword, "")
    fileSaver.saveTextToFile(PASSWORDS_FILENAME, encodedText)
  }
  
  override fun isCorrect(masterPassword: String): Boolean {
    val textFromFile = fileSaver.readTextFromFile(PASSWORDS_FILENAME)
    return try {
      val metaInfo = cryptography.getEncryptionMetaInfo(masterPassword, textFromFile)
      cryptography.decryptCipher(masterPassword, metaInfo, textFromFile)
      true
    } catch (e: Throwable) {
      false
    }
  }
}