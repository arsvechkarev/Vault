package com.arsvechkarev.vault.features.common

import android.content.Context
import androidx.core.hardware.fingerprint.FingerprintManagerCompat

class AndroidFingerprintsChecker(private val context: Context) : FingerprintsChecker {
  
  override fun areFingerprintsSupported(): Boolean {
    val fingerprintManagerCompat = FingerprintManagerCompat.from(context)
    return when {
      !fingerprintManagerCompat.isHardwareDetected -> false
      else -> fingerprintManagerCompat.hasEnrolledFingerprints()
    }
  }
}