package com.arsvechkarev.vault.features.common.data.files

import com.arsvechkarev.vault.features.common.data.preferences.Preferences

class StorageBackupPreferences(private val preferences: Preferences) {
  
  suspend fun getStorageBackupEnabledPair(): Pair<Boolean, String?> {
    val backupEnabled = preferences.getBoolean(KEY_STORAGE_BACKUP_ENABLED)
    val uri = preferences.getString(KEY_STORAGE_BACKUP_FOLDER_URI)
    return backupEnabled to uri
  }
  
  suspend fun enableBackup(uri: String) {
    preferences.putAll(
      mapOf(
        KEY_STORAGE_BACKUP_ENABLED to true,
        KEY_STORAGE_BACKUP_FOLDER_URI to uri,
      )
    )
  }
  
  suspend fun disableBackup() {
    preferences.putBoolean(KEY_STORAGE_BACKUP_ENABLED, false)
  }
  
  suspend fun getLatestBackupTimestamp(): Long {
    return preferences.getLong(KEY_STORAGE_BACKUP_LATEST_TIMESTAMP)
  }
  
  suspend fun getBackupPerformedAt(): Int {
    return preferences.getInt(KEY_STORAGE_BACKUP_PERFORMED_AT_COUNT)
  }
  
  suspend fun saveBackupMetadata(timestamp: Long, performedAtCount: Int) {
    preferences.putAll(
      mapOf(
        KEY_STORAGE_BACKUP_LATEST_TIMESTAMP to timestamp,
        KEY_STORAGE_BACKUP_PERFORMED_AT_COUNT to performedAtCount,
      )
    )
  }
  
  private companion object {
    
    const val KEY_STORAGE_BACKUP_ENABLED = "storage_backup_enabled"
    const val KEY_STORAGE_BACKUP_FOLDER_URI = "storage_backup_folder_uri"
    const val KEY_STORAGE_BACKUP_LATEST_TIMESTAMP = "storage_backup_latest_timestamp"
    const val KEY_STORAGE_BACKUP_PERFORMED_AT_COUNT = "storage_backup_performed_at_count"
  }
}