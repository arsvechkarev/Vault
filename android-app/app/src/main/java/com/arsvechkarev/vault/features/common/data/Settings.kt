package com.arsvechkarev.vault.features.common.data

import android.content.SharedPreferences
import androidx.core.content.edit

interface Settings {
  
  suspend fun getAll(): Map<String, Any?>
  
  suspend fun setBoolean(key: String, value: Boolean)
  
  suspend fun getBoolean(key: String): Boolean
}

class SharedPreferencesSettings(
  private val sharedPreferences: SharedPreferences
) : Settings {
  
  override suspend fun getAll(): Map<String, Any?> {
    return sharedPreferences.all as Map<String, Any?>
  }
  
  override suspend fun setBoolean(key: String, value: Boolean) {
    sharedPreferences.edit(commit = true) { putBoolean(key, value) }
  }
  
  override suspend fun getBoolean(key: String): Boolean {
    return sharedPreferences.getBoolean(key, false)
  }
}
