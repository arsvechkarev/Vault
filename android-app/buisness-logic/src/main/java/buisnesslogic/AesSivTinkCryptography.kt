package buisnesslogic

import com.arsvechkarev.commoncrypto.AesSivTinkCipher

object AesSivTinkCryptography : Cryptography {
  
  override fun encryptData(password: String, plaintext: String): ByteArray {
    return AesSivTinkCipher.encrypt(password, plaintext)
  }
  
  override fun decryptData(password: String, ciphertext: ByteArray): String {
    return AesSivTinkCipher.decrypt(password, ciphertext)
  }
}
