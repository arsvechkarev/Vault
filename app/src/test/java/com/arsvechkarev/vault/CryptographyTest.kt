package com.arsvechkarev.vault

import com.arsvechkarev.vault.cryptography.Cryptography
import com.arsvechkarev.vault.cryptography.SeedRandomGeneratorImpl
import org.junit.Assert.assertEquals
import org.junit.Test

class CryptographyTest {
  
  private val cryptography = Cryptography(JavaBase64Coder, SeedRandomGeneratorImpl)
  
  @Test
  fun `Encrypting and decrypting data`() {
    val password = "ko)du28:0=_"
    val originalData = "this is super secret data"
    
    val ciphertext = cryptography.encryptForTheFirstTime(password, originalData)
    val metaInfo = cryptography.getEncryptionMetaInfo(password, ciphertext)
    val decryptedText = cryptography.decryptCipher(password, metaInfo, ciphertext)
    
    assertEquals(originalData, decryptedText)
    
    val newData = "now this is new data"
    val newCiphertext = cryptography.encryptData(password, metaInfo, newData)
    val newDecryptedData = cryptography.decryptCipher(password, metaInfo, newCiphertext)
    
    assertEquals(newData, newDecryptedData)
  }
  
  @Test
  fun `Testing meta info`() {
    val password = "io00(3zUIc"
    val oldData = "this is old data"
    
    val initialCipher = cryptography.encryptForTheFirstTime(password, oldData)
    val oldMetaInfo = cryptography.getEncryptionMetaInfo(password, initialCipher)
    
    val newData = "new data"
    val newCipher = cryptography.encryptData(password, oldMetaInfo, newData)
    val newMetaInfo = cryptography.getEncryptionMetaInfo(password, newCipher)
    
    assertEquals(oldMetaInfo, newMetaInfo)
  }
}