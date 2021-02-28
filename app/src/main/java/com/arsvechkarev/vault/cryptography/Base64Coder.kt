package com.arsvechkarev.vault.cryptography

import com.arsvechkarev.vault.core.Base64

interface Base64Coder {
  
  fun encode(byteArray: ByteArray): String
  
  fun decode(string: String): ByteArray
}

object JavaBase64Coder : Base64Coder {
  
  override fun encode(byteArray: ByteArray): String {
    return Base64.getEncoder().encodeToString(byteArray)
  }
  
  override fun decode(string: String): ByteArray {
    return Base64.getDecoder().decode(string)
  }
}