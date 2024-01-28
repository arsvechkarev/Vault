package com.arsvechkarev.vault.stubs

import com.arsvechkarev.vault.features.common.data.preferences.Preferences

class StubPreferences : Preferences {
  
  private val prefs = HashMap<String, Any?>()
  
  override suspend fun getAll(): Map<String, Any?> {
    return prefs
  }
  
  override suspend fun putAll(map: Map<String, Any?>) {
    prefs.putAll(map)
  }
  
  override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
    return prefs[key] as? Boolean ?: defaultValue
  }
  
  override suspend fun putBoolean(key: String, value: Boolean) {
    prefs[key] = value
  }
  
  override suspend fun getString(key: String): String? {
    return prefs[key] as? String
  }
  
  override suspend fun putString(key: String, value: String) {
    prefs[key] = value
  }
  
  override suspend fun getLong(key: String): Long {
    return prefs[key] as? Long ?: 0L
  }
  
  override suspend fun putLong(key: String, value: Long) {
    prefs[key] = value
  }
  
  override suspend fun getInt(key: String): Int {
    return prefs[key] as? Int ?: 0
  }
  
  override suspend fun putInt(key: String, value: Int) {
    prefs[key] = value
  }
  
  @Suppress("UNCHECKED_CAST")
  override suspend fun getStringSet(key: String): Set<String>? {
    return (prefs[key] as? Set<*>) as? Set<String>
  }
  
  override suspend fun putStringSet(key: String, value: Set<String>) {
    prefs[key] = value
  }
  
  override suspend fun clear() {
    prefs.clear()
  }
}