package com.arsvechkarev.vault.features.common.domain

import app.keemobile.kotpass.database.KeePassDatabase
import com.arsvechkarev.vault.features.common.data.files.StorageBackupExternalFileSaver
import com.arsvechkarev.vault.features.common.data.files.StorageBackupPreferences
import com.arsvechkarev.vault.features.common.model.BackupFileData

class StorageBackupInteractor(
  private val storageBackupExternalFileSaver: StorageBackupExternalFileSaver,
  private val preferences: StorageBackupPreferences,
  private val journal: DatabaseChangesJournal,
  private val timestampProvider: TimestampProvider,
  private val dateTimeFormatter: DateTimeFormatter,
  private val passedTimeSinceLastBackupThreshold: Long,
  private val databaseChangesThreshold: Int,
  private val backupFileCountThreshold: Int,
) {
  
  suspend fun forceBackup(database: KeePassDatabase) {
    val (enabled, backupFolder) = preferences.getStorageBackupEnabledPair()
    if (!enabled || backupFolder == null) {
      return
    }
    performBackupInternal(backupFolder, database)
  }
  
  suspend fun performBackupIfNeeded(database: KeePassDatabase) {
    val (enabled, backupFolder) = preferences.getStorageBackupEnabledPair()
    if (!enabled || backupFolder == null) {
      return
    }
    val databaseChangesSinceLastBackup = journal.getChangeCount() - preferences.getBackupPerformedAt()
    if (timeSinceLastBackupPassedThreshold() && databaseChangesSinceLastBackup > 0) {
      performBackupInternal(backupFolder, database)
    } else if (databaseChangesSinceLastBackup >= databaseChangesThreshold) {
      performBackupInternal(backupFolder, database)
    }
  }
  
  private suspend fun timeSinceLastBackupPassedThreshold(): Boolean {
    val latestBackupTimestamp = preferences.getLatestBackupTimestamp()
    val now = timestampProvider.now()
    return now - latestBackupTimestamp > passedTimeSinceLastBackupThreshold
  }
  
  private suspend fun performBackupInternal(directory: String, database: KeePassDatabase) {
    val backupFiles = storageBackupExternalFileSaver.getAll(directory)
    if (backupFiles.size >= backupFileCountThreshold) {
      removeAllOlderFiles(directory, backupFiles)
    }
    val newFilename = generateFilename()
    storageBackupExternalFileSaver.saveDatabase(directory, newFilename, database)
    preferences.saveBackupMetadata(timestampProvider.now(), journal.getChangeCount())
  }
  
  private fun generateFilename(): String {
    return "file_${dateTimeFormatter.formatSimple(timestampProvider.now())}.kdbx"
  }
  
  private suspend fun removeAllOlderFiles(directory: String, backupFiles: List<BackupFileData>) {
    val extraFiles = backupFiles.sortedBy { it.lastModified }
        .take(backupFiles.size - backupFileCountThreshold + 1)
    storageBackupExternalFileSaver.deleteAll(directory, extraFiles)
  }
}
