package com.arsvechkarev.vault.features.common.biometrics

import android.content.Context
import android.os.Build
import androidx.core.hardware.fingerprint.FingerprintManagerCompat

class BiometricsAvailabilityCheckerImpl(private val context: Context) :
    BiometricsAvailabilityChecker {

    override fun isBiometricsSupported(): Boolean {
        // Biometrics is available if:
        //  1. Device supports fingerprints
        //  2. User has enrolled fingerprints
        //  3. Api level >= 28, (Android Pie+) so that we can show BiometricPrompt
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return false
        }
        val fingerprintManagerCompat = FingerprintManagerCompat.from(context)
        return when {
            !fingerprintManagerCompat.isHardwareDetected -> false
            else -> fingerprintManagerCompat.hasEnrolledFingerprints()
        }
    }
}