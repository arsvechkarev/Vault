package com.arsvechkarev.vault.common.biometrics

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
    passwordFileSaver.saveData(encryptedInfo)
    ivFileSaver.saveData(cipher.iv)
  }
  
  @WorkerThread
  fun getIv(): ByteArray {
    return checkNotNull(ivFileSaver.readData()) { "Iv shouldn't be null" }
  }
  
  @WorkerThread
  fun getMasterPassword(result: BiometricPrompt.AuthenticationResult): String {
    val cipher = result.cryptoObject?.cipher ?: throw NullPointerException()
    val encodedData = passwordFileSaver.readData()
    return String(cipher.doFinal(encodedData), charset)
  }
  
  @WorkerThread
  fun deleteAllFiles() {
    passwordFileSaver.delete()
    ivFileSaver.delete()
  }
  
  companion object {
    
    private val charset = Charsets.UTF_8
    
    const val BIOMETRICS_PASSWORD_FILENAME = "enc.passwrd"
    const val BIOMETRICS_IV_FILENAME = "enc.iv"
  }
}