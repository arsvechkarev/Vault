package com.arsvechkarev.vault.features.common.biometrics

import android.content.Context
import com.arsvechkarev.vault.features.common.data.AndroidSharedPreferences
import java.util.Base64

interface BiometricsEnabledProvider {
  
  suspend fun isBiometricsEnabled(): Boolean
}

interface BiometricsStorage {
  
  suspend fun saveBiometricsData(encryptedPassword: ByteArray, initializationVector: ByteArray)
  
  suspend fun getBiometricsData(): Pair<ByteArray, ByteArray>
  
  suspend fun clear()
}

class BiometricsStorageImpl(context: Context) : BiometricsEnabledProvider, BiometricsStorage {
  
  private val prefs = AndroidSharedPreferences(
    context.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE)
  )
  
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
    
    const val PREFERENCES_FILENAME = "biometrics_data"
    const val KEY_PASSWORD = "password"
    const val KEY_IV = "iv"
  }
}
