package com.arsvechkarev.vault.features.common.biometrics

import android.content.Context
import androidx.biometric.BiometricManager

interface BiometricsAvailabilityProvider {
  
  fun isAvailable(): Boolean
}

class BiometricsAvailabilityProviderImpl(
  private val context: Context
) : BiometricsAvailabilityProvider {
  
  override fun isAvailable(): Boolean {
    return BiometricManager.from(context).canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
  }
}
