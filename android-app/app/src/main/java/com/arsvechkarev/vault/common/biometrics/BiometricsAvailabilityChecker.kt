package com.arsvechkarev.vault.common.biometrics

/**
 * Checks whether biometrics is supported
 */
interface BiometricsAvailabilityChecker {
  
  fun isBiometricsSupported(): Boolean
}