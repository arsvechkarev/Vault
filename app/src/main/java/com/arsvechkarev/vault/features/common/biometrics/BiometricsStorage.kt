package com.arsvechkarev.vault.features.common.biometrics

import androidx.annotation.WorkerThread
import androidx.biometric.BiometricPrompt
import buisnesslogic.FileSaver
import buisnesslogic.base64.Base64Coder

class BiometricsStorage constructor(
  private val passwordFileSaver: FileSaver,
  private val ivFileSaver: FileSaver,
  private val base64Coder: Base64Coder
) {
  
  @WorkerThread
  fun encryptAndSaveData(masterPassword: String, result: BiometricPrompt.AuthenticationResult) {
    val cipher = result.cryptoObject?.cipher ?: throw NullPointerException()
    val encryptedInfo = cipher.doFinal(masterPassword.toByteArray(charset))
    passwordFileSaver.saveTextToFile(base64Coder.encode(encryptedInfo))
    ivFileSaver.saveTextToFile(base64Coder.encode(cipher.iv))
  }
  
  @WorkerThread
  fun getIv(): ByteArray {
    return base64Coder.decode(ivFileSaver.readTextFromFile())
  }
  
  @WorkerThread
  fun getMasterPassword(result: BiometricPrompt.AuthenticationResult): String {
    val cipher = result.cryptoObject?.cipher ?: throw NullPointerException()
    val encodedString = passwordFileSaver.readTextFromFile()
    return String(cipher.doFinal(base64Coder.decode(encodedString)), charset)
  }
  
  @WorkerThread
  fun deleteAllFiles() {
    passwordFileSaver.deleteFile()
    ivFileSaver.deleteFile()
  }
  
  companion object {
    
    private val charset = Charsets.UTF_8
    
    const val BIOMETRICS_PASSWORD_FILENAME = "enc.passwrd"
    const val BIOMETRICS_IV_FILENAME = "enc.iv"
  }
}