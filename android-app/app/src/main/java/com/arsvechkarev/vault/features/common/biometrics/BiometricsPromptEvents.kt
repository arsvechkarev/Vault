package com.arsvechkarev.vault.features.common.biometrics

import androidx.biometric.BiometricPrompt

/**
 * Events of biometrics dialog
 */
sealed class BiometricsPromptEvents {
  
  /** Failure, i.e when user tried to use fingerprint, but couldn't */
  object Failure : BiometricsPromptEvents()
  
  /**  Error, i.e authentication with prompt failed or was cancelled by user */
  open class Error(val errorString: CharSequence) : BiometricsPromptEvents()
  
  /** Prompt was cancelled by user */
  class Cancelled(errorString: CharSequence) : Error(errorString)
  
  /** User made too many attempts and prompt was locked out */
  class Lockout(errorString: CharSequence) : Error(errorString)
  
  /** User made way too many attempts and prompt was locked out permanently */
  class LockoutPermanent(errorString: CharSequence) : Error(errorString)
  
  /** Success with biometric prompt */
  class Success(val result: BiometricPrompt.AuthenticationResult) : BiometricsPromptEvents()
}