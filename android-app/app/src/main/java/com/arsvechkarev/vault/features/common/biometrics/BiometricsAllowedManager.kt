package com.arsvechkarev.vault.features.common.biometrics

import com.arsvechkarev.vault.BuildConfig
import com.arsvechkarev.vault.features.common.data.preferences.Preferences
import com.arsvechkarev.vault.features.common.domain.TimestampProvider
import java.util.concurrent.TimeUnit

interface BiometricsAllowedManager {
  
  suspend fun isBiometricsEnterAllowed(): Boolean
  
  suspend fun markBiometricsEnter()
  
  suspend fun resetBiometricsStats()
}

class BiometricsAllowedManagerImpl(
  private val timestampProvider: TimestampProvider,
  private val preferences: Preferences
) : BiometricsAllowedManager {
  
  
  override suspend fun isBiometricsEnterAllowed(): Boolean {
    val lastEnterTimestamp = preferences.getLong(KEY_LAST_PASSWORD_ENTER_TIMESTAMP)
    if (timestampProvider.now() - lastEnterTimestamp > MAX_TIME_SINCE_LAST_PASSWORD_ENTER) {
      return false
    }
    return preferences.getLong(KEY_SUCCESSIVE_ENTER_COUNT) < MAX_SUCCESSIVE_BIOMETRICS_ENTERS
  }
  
  override suspend fun markBiometricsEnter() {
    var enterCount = preferences.getLong(KEY_SUCCESSIVE_ENTER_COUNT)
    preferences.putLong(KEY_SUCCESSIVE_ENTER_COUNT, ++enterCount)
  }
  
  override suspend fun resetBiometricsStats() {
    preferences.putLong(KEY_LAST_PASSWORD_ENTER_TIMESTAMP, timestampProvider.now())
    preferences.putLong(KEY_SUCCESSIVE_ENTER_COUNT, 0L)
  }
  
  private companion object {
    
    const val KEY_LAST_PASSWORD_ENTER_TIMESTAMP = "last_password_enter_timestamp"
    const val KEY_SUCCESSIVE_ENTER_COUNT = "successive_biometrics_enter_count"
    
    val MAX_SUCCESSIVE_BIOMETRICS_ENTERS = if (BuildConfig.DEBUG) 5 else 15
    
    val MAX_TIME_SINCE_LAST_PASSWORD_ENTER = if (BuildConfig.DEBUG) {
      TimeUnit.MINUTES.toMillis(1)
    } else {
      TimeUnit.DAYS.toMillis(7)
    }
  }
}