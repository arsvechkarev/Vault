package com.arsvechkarev.vault.features.common.fingerprints

import android.content.Context
import androidx.core.hardware.fingerprint.FingerprintManagerCompat

class FingerprintsAvailabilityCheckerImpl(private val context: Context) :
  FingerprintsAvailabilityChecker {
  
  override fun areFingerprintsSupported(): Boolean {
    val fingerprintManagerCompat = FingerprintManagerCompat.from(context)
    return when {
      !fingerprintManagerCompat.isHardwareDetected -> false
      else -> fingerprintManagerCompat.hasEnrolledFingerprints()
    }
  }
}