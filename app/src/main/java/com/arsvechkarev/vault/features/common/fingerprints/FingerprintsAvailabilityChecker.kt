package com.arsvechkarev.vault.features.common.fingerprints

interface FingerprintsAvailabilityChecker {
  
  fun areFingerprintsSupported(): Boolean
}