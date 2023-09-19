package buisnesslogic

import com.arsvechkarev.commoncrypto.AesSivTinkCipher

// TODO (9/19/23): Delete
object AesSivTinkCryptography : Cryptography {
  
  override fun encryptData(password: Password, plaintext: String): ByteArray {
    return AesSivTinkCipher.encrypt(password.rawValue, plaintext)
  }
  
  override fun decryptData(password: Password, ciphertext: ByteArray): String {
    return AesSivTinkCipher.decrypt(password.rawValue, ciphertext)
  }
}
