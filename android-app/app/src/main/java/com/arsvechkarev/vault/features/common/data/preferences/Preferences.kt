package com.arsvechkarev.vault.features.common.data.preferences

interface Preferences {
  
  suspend fun getAll(): Map<String, Any?>
  
  suspend fun putAll(map: Map<String, Any?>)
  
  suspend fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
  
  suspend fun putBoolean(key: String, value: Boolean)
  
  suspend fun getString(key: String): String?
  
  suspend fun putString(key: String, value: String)
  
  suspend fun getLong(key: String): Long
  
  suspend fun putLong(key: String, value: Long)
  
  suspend fun getInt(key: String): Int
  
  suspend fun putInt(key: String, value: Int)
  
  suspend fun getStringSet(key: String): Set<String>?
  
  suspend fun putStringSet(key: String, value: Set<String>)
  
  suspend fun clear()
}
