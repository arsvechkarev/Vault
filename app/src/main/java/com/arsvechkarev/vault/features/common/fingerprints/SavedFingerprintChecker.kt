package com.arsvechkarev.vault.features.common.fingerprints

interface SavedFingerprintChecker {
  
  fun isAuthorizationWithUserFingerAvailable(): Boolean
  
  fun setAuthorizationWithUserFingerAvailable(available: Boolean)
}