package com.arsvechkarev.vault.features.common.fingerprints

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE

class SavedFingerprintCheckerImpl(context: Context) : SavedFingerprintChecker {
  
  private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_FILENAME, MODE_PRIVATE)
  
  override fun isAuthorizationWithUserFingerAvailable(): Boolean {
    return sharedPrefs.getBoolean(IS_AUTH_WITH_FINGER_AVAILABLE, false)
  }
  
  @SuppressLint("ApplySharedPref")
  override fun setAuthorizationWithUserFingerAvailable(available: Boolean) {
    sharedPrefs.edit().putBoolean(IS_AUTH_WITH_FINGER_AVAILABLE, available).commit()
  }
  
  private companion object {
    
    const val SHARED_PREFS_FILENAME = "SavedFingerprintChecker"
    const val IS_AUTH_WITH_FINGER_AVAILABLE = "IS_AUTH_WITH_FINGER_AVAILABLE"
  }
}