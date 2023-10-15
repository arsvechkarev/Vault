package com.arsvechkarev.vault.features.common.biometrics

import javax.crypto.Cipher

interface BiometricsCryptography {
  
  suspend fun perform(data: ByteArray): ByteArray
}

class BiometricsCryptographyImpl(
  private val cipher: Cipher
) : BiometricsCryptography {
  
  override suspend fun perform(data: ByteArray): ByteArray {
    return cipher.doFinal(data)
  }
}
