package busnesslogic

import buisnesslogic.Cryptography
import buisnesslogic.base64.JavaBase64Coder
import buisnesslogic.random.SeedRandomGeneratorImpl
import org.junit.Assert.assertEquals
import org.junit.Test

class CryptographyTest {
  
  private val cryptography = Cryptography(JavaBase64Coder, SeedRandomGeneratorImpl)
  
  @Test
  fun `Encrypting and decrypting data`() {
    val password = "ko)du28:0=_"
    val plaintext = "this is super secret data"
  
    val ciphertext = cryptography.encryptForTheFirstTime(password, plaintext)
    val decryptedText = cryptography.decryptData(password, ciphertext)
  
    assertEquals(plaintext, decryptedText)
  
    val newData = "now this is new data"
    val newCiphertext = cryptography.encryptData(password, newData, ciphertext)
    val newDecryptedData = cryptography.decryptData(password, newCiphertext)
  
    assertEquals(newData, newDecryptedData)
  }
}