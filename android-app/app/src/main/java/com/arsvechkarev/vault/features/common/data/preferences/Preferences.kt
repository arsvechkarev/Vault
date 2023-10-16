package com.arsvechkarev.vault.features.common.data.preferences

interface Preferences {
  
  suspend fun getAll(): Map<String, Any?>
  
  suspend fun saveAll(map: Map<String, Any?>)
  
  suspend fun getBoolean(key: String): Boolean
  
  suspend fun saveBoolean(key: String, value: Boolean)
  
  suspend fun getString(key: String): String?
  
  suspend fun saveString(key: String, value: String)
  
  suspend fun getLong(key: String): Long
  
  suspend fun saveLong(key: String, value: Long)
  
  suspend fun getStringSet(key: String): Set<String>?
  
  suspend fun saveStringSet(key: String, value: Set<String>)
  
  suspend fun clear()
}
