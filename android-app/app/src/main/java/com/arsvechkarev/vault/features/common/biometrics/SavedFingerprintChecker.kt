package com.arsvechkarev.vault.features.common.biometrics

/**
 * Saves whether user has fingerprint authentication or not
 */
interface SavedFingerprintChecker {

    /** True if user has fingerprint authentication available, false otherwise */
    fun isAuthorizationWithUserFingerAvailable(): Boolean

    /** Saves whether authentication is available */
    fun setAuthorizationWithUserFingerAvailable(available: Boolean)
}