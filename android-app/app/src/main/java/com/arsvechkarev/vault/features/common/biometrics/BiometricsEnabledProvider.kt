package com.arsvechkarev.vault.features.common.biometrics

import com.arsvechkarev.vault.features.common.data.preferences.Preferences
import java.util.Base64

interface BiometricsEnabledProvider {
  
  suspend fun isBiometricsEnabled(): Boolean
}

interface BiometricsStorage {
  
  suspend fun saveBiometricsData(encryptedPassword: ByteArray, initializationVector: ByteArray)
  
  suspend fun getBiometricsData(): Pair<ByteArray, ByteArray>
  
  suspend fun clear()
}

class BiometricsStorageImpl(
  private val prefs: Preferences
) : BiometricsEnabledProvider, BiometricsStorage {
  
  override suspend fun isBiometricsEnabled(): Boolean {
    return prefs.getString(KEY_PASSWORD) != null
  }
  
  override suspend fun saveBiometricsData(
    encryptedPassword: ByteArray,
    initializationVector: ByteArray
  ) {
    prefs.saveAll(
      mapOf(
        KEY_PASSWORD to String(Base64.getEncoder().encode(encryptedPassword)),
        KEY_IV to String(Base64.getEncoder().encode(initializationVector))
      )
    )
  }
  
  override suspend fun getBiometricsData(): Pair<ByteArray, ByteArray> {
    val passwordString = checkNotNull(prefs.getString(KEY_PASSWORD))
    val passwordBytes = Base64.getDecoder().decode(passwordString)
    val iv = Base64.getDecoder().decode(checkNotNull(prefs.getString(KEY_IV)))
    return passwordBytes to iv
  }
  
  override suspend fun clear() {
    prefs.clear()
  }
  
  private companion object {
    
    const val KEY_PASSWORD = "password"
    const val KEY_IV = "iv"
  }
}
