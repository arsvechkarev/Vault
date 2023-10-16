package com.arsvechkarev.vault.features.common.biometrics

import javax.crypto.Cipher

interface BiometricsCryptography {
  
  suspend fun perform(data: ByteArray): ByteArray
  
  val iv: ByteArray
  
  companion object {
    
    fun create(cipher: Cipher): BiometricsCryptography {
      return BiometricsCryptographyImpl(cipher)
    }
  }
}

class BiometricsCryptographyImpl(
  private val cipher: Cipher
) : BiometricsCryptography {
  
  override suspend fun perform(data: ByteArray): ByteArray {
    return cipher.doFinal(data)
  }
  
  override val iv: ByteArray get() = cipher.iv
}
