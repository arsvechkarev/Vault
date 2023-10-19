package com.arsvechkarev.vault.features.common.domain

import app.keemobile.kotpass.database.KeePassDatabase
import com.arsvechkarev.vault.features.common.data.StorageBackupFileSaver
import com.arsvechkarev.vault.features.common.data.StorageBackupPreferences
import com.arsvechkarev.vault.features.common.model.BackupFileData

class StorageBackupInteractor(
  private val fileSaver: StorageBackupFileSaver,
  private val preferences: StorageBackupPreferences,
  private val journal: DatabaseChangesJournal,
  private val timestampProvider: TimestampProvider,
  private val dateTimeFormatter: DateTimeFormatter,
  private val passedTimeSinceLastBackupThreshold: Long,
  private val databaseChangesThreshold: Int,
  private val backupFileCountThreshold: Int,
) {
  
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
    val latestBackupTimestamp = preferences.getBackupTimestamp()
    val now = timestampProvider.now()
    return now - latestBackupTimestamp > passedTimeSinceLastBackupThreshold
  }
  
  private suspend fun performBackupInternal(directory: String, database: KeePassDatabase) {
    val backupFiles = fileSaver.getAll(directory)
    if (backupFiles.size >= backupFileCountThreshold) {
      removeAllOlderFiles(directory, backupFiles)
    }
    val newFilename = generateFilename()
    fileSaver.saveDatabase(directory, newFilename, database)
    preferences.saveBackupMetadata(journal.getChangeCount(), timestampProvider.now())
  }
  
  private fun generateFilename(): String {
    return "file_${dateTimeFormatter.format(timestampProvider.now())}.kdbx"
  }
  
  private suspend fun removeAllOlderFiles(directory: String, backupFiles: List<BackupFileData>) {
    val extraFiles = backupFiles.sortedBy { it.lastModified }
        .take(backupFiles.size - backupFileCountThreshold + 1)
    fileSaver.deleteAll(directory, extraFiles)
  }
}
