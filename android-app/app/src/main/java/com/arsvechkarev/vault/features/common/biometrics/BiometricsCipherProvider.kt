package com.arsvechkarev.vault.features.common.biometrics

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

interface BiometricsCipherProvider {
  
  fun getInitializedCipherForEncryption(): Cipher
  
  fun getInitializedCipherForDecryption(initializationVector: ByteArray): Cipher
}

class BiometricsCipherProviderImpl : BiometricsCipherProvider {
  
  override fun getInitializedCipherForEncryption(): Cipher {
    val cipher = getCipher()
    val secretKey = getOrCreateSecretKey()
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    return cipher
  }
  
  override fun getInitializedCipherForDecryption(
    initializationVector: ByteArray
  ): Cipher {
    val cipher = getCipher()
    val secretKey = getOrCreateSecretKey()
    cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, initializationVector))
    return cipher
  }
  
  private fun getCipher(): Cipher {
    val transformation = "$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING"
    return Cipher.getInstance(transformation)
  }
  
  private fun getOrCreateSecretKey(): SecretKey {
    // If Secretkey was previously created for that keyName, then grab and return it.
    val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
    keyStore.load(null) // Keystore must be loaded before it can be accessed
    keyStore.getKey(SECRET_KEY_NAME, null)?.let { return it as SecretKey }
    
    // if you reach here, then a new SecretKey must be generated for that keyName
    val paramsBuilder = KeyGenParameterSpec.Builder(
      SECRET_KEY_NAME,
      KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
    paramsBuilder.apply {
      setBlockModes(ENCRYPTION_BLOCK_MODE)
      setEncryptionPaddings(ENCRYPTION_PADDING)
      setKeySize(KEY_SIZE)
      setUserAuthenticationRequired(true)
    }
    
    val keyGenParams = paramsBuilder.build()
    val keyGenerator = KeyGenerator.getInstance(
      KeyProperties.KEY_ALGORITHM_AES,
      ANDROID_KEYSTORE
    )
    keyGenerator.init(keyGenParams)
    return keyGenerator.generateKey()
  }
  
  companion object {
    
    private const val KEY_SIZE = 256
    private const val SECRET_KEY_NAME = "vault_secret_key"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
    private const val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
    private const val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
  }
}
