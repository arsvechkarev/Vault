package com.arsvechkarev.commoncrypto

import com.google.crypto.tink.subtle.AesSiv
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object AesSivTinkCipher {
  
  fun encrypt(masterPassword: String, plaintext: String): ByteArray {
    val hashedPassword = MessageDigest.getInstance("SHA-512")
        .digest(masterPassword.toByteArray(StandardCharsets.UTF_8))
    val daead = AesSiv(hashedPassword)
    return daead.encryptDeterministically(
      plaintext.toByteArray(StandardCharsets.UTF_8), null)
  }
  
  fun decrypt(masterPassword: String, ciphertext: ByteArray): String {
    val hashedPassword = MessageDigest.getInstance("SHA-512")
        .digest(masterPassword.toByteArray(StandardCharsets.UTF_8))
    val daead = AesSiv(hashedPassword)
    return String(daead.decryptDeterministically(ciphertext, null),
      StandardCharsets.UTF_8)
  }
}
