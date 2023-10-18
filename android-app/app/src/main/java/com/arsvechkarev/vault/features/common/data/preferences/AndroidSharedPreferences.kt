package com.arsvechkarev.vault.features.common.data.preferences

import android.content.SharedPreferences
import androidx.core.content.edit

class AndroidSharedPreferences(
  private val sharedPreferences: SharedPreferences
) : Preferences {
  
  override suspend fun getAll(): Map<String, Any?> {
    return sharedPreferences.all
  }
  
  @Suppress("UNCHECKED_CAST")
  override suspend fun putAll(map: Map<String, Any?>) {
    sharedPreferences.edit(commit = true) {
      map.forEach { (key, value) ->
        when (value) {
          is Int -> putInt(key, value)
          is Long -> putLong(key, value)
          is String -> putString(key, value)
          is Boolean -> putBoolean(key, value)
          is Float -> putFloat(key, value)
          is Set<*> -> putStringSet(key, value as Set<String>)
        }
      }
    }
  }
  
  override suspend fun putBoolean(key: String, value: Boolean) {
    sharedPreferences.edit(commit = true) { putBoolean(key, value) }
  }
  
  override suspend fun getBoolean(key: String): Boolean {
    return sharedPreferences.getBoolean(key, false)
  }
  
  override suspend fun getString(key: String): String? {
    return sharedPreferences.getString(key, null)
  }
  
  override suspend fun putString(key: String, value: String) {
    sharedPreferences.edit(commit = true) { putString(key, value) }
  }
  
  override suspend fun getLong(key: String): Long {
    return sharedPreferences.getLong(key, 0L)
  }
  
  override suspend fun putLong(key: String, value: Long) {
    sharedPreferences.edit(commit = true) { putLong(key, value) }
  }
  
  override suspend fun getInt(key: String): Int {
    return sharedPreferences.getInt(key, 0)
  }
  
  override suspend fun putInt(key: String, value: Int) {
    sharedPreferences.edit(commit = true) { putInt(key, value) }
  }
  
  override suspend fun getStringSet(key: String): Set<String>? {
    return sharedPreferences.getStringSet(key, null)
  }
  
  override suspend fun putStringSet(key: String, value: Set<String>) {
    sharedPreferences.edit(commit = true) { putStringSet(key, value) }
  }
  
  override suspend fun clear() {
    sharedPreferences.edit(commit = true) { clear() }
  }
}
