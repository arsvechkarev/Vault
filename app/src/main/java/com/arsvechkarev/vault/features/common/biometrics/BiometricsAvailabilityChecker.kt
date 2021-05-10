package com.arsvechkarev.vault.features.common.biometrics

/**
 * Checks whether biometrics is supported
 */
interface BiometricsAvailabilityChecker {
  
  fun isBiometricsSupported(): Boolean
}