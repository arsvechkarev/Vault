package com.arsvechkarev.vault.features.common.biometrics

import kotlinx.coroutines.flow.Flow

/**
 * Prompt for biometrics authentication
 */
interface BiometricsPrompt {
  
  /** Returns flow of biometrics events. See [BiometricsPromptEvents] */
  fun biometricsEvents(): Flow<BiometricsPromptEvents>
  
  /** Open prompt for encryption */
  fun showEncryptingBiometricsPrompt()
  
  /**
   * Open prompt for decryption
   *
   * @param iv Initialization vector that was used in encryption
   */
  fun showDecryptingBiometricsPrompt(iv: ByteArray)
}