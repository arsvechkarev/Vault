package com.arsvechkarev.vault.features.common.biometrics

sealed interface BiometricsEvent {
  
  class Success(val cryptography: BiometricsCryptography) : BiometricsEvent
  
  class Error(val error: ErrorType) : BiometricsEvent
  
  enum class ErrorType {
    CANCELLED, LOCKOUT, LOCKOUT_PERMANENT, OTHER
  }
}