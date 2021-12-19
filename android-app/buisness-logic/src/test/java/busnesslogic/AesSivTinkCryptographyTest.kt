package busnesslogic

import buisnesslogic.AesSivTinkCryptography
import org.junit.Assert.assertEquals
import org.junit.Test

class AesSivTinkCryptographyTest {
  
  @Test
  fun `Encrypting and decrypting data`() {
    val password = "ko)du28:0=_"
    val plaintext = "this is super secret data"
    
    val ciphertext = AesSivTinkCryptography.encryptData(password, plaintext)
    val decryptedText = AesSivTinkCryptography.decryptData(password, ciphertext)
    
    assertEquals(plaintext, decryptedText)
    
    val newData = "now this is new data"
    val newCiphertext = AesSivTinkCryptography.encryptData(password, newData)
    val newDecryptedData = AesSivTinkCryptography.decryptData(password, newCiphertext)
    
    assertEquals(newData, newDecryptedData)
  }
}