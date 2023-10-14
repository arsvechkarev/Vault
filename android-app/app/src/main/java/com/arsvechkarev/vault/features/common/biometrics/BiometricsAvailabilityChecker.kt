package com.arsvechkarev.vault.features.common.biometrics

import android.content.Context
import androidx.biometric.BiometricManager

interface BiometricsAvailabilityChecker {
  
  fun isAvailable(): Boolean
}

class BiometricsAvailabilityCheckerImpl(
  private val context: Context
) : BiometricsAvailabilityChecker {
  
  override fun isAvailable(): Boolean {
    return BiometricManager.from(context).canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
  }
}
