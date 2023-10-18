package com.arsvechkarev.vault.features.common.domain

import app.keemobile.kotpass.database.KeePassDatabase
import com.arsvechkarev.vault.features.common.data.StorageBackupFileSaver
import com.arsvechkarev.vault.features.common.data.StorageBackupPreferences
import com.arsvechkarev.vault.features.common.model.BackupFileData
import java.util.concurrent.TimeUnit

class StorageBackupInteractor(
  private val fileSaver: StorageBackupFileSaver,
  private val preferences: StorageBackupPreferences,
  private val journal: DatabaseChangesJournal,
  private val timestampProvider: TimestampProvider,
  private val dateTimeFormatter: DateTimeFormatter,
  private val passedTimeSinceLastBackupThreshold: Long,
  private val databaseChangesThreshold: Int,
) {
  
  suspend fun performBackupIfNeeded(database: KeePassDatabase) {
    val (enabled, backupFolder) = preferences.getStorageBackupEnabledPair()
    if (!enabled || backupFolder == null) {
      return
    }
    val databaseChangesSinceLastBackup = journal.getChangeCount() - preferences.getBackupPerformedAt()
    if (timeSinceLastBackupPassedThreshold() && databaseChangesSinceLastBackup > 0) {
      performBackupInternal(backupFolder, database)
    } else if (databaseChangesSinceLastBackup > databaseChangesThreshold) {
      performBackupInternal(backupFolder, database)
    }
  }
  
  private suspend fun timeSinceLastBackupPassedThreshold(): Boolean {
    val latestBackupTimestamp = preferences.getBackupTimestamp()
    val now = timestampProvider.now()
    return now - latestBackupTimestamp > passedTimeSinceLastBackupThreshold
  }
  
  private suspend fun performBackupInternal(backupFolder: String, database: KeePassDatabase) {
    val backupFiles: List<BackupFileData> = fileSaver.getAll(backupFolder)
    if (backupFiles.size >= THRESHOLD_FILES_COUNT) {
      removeAllOlderFiles(backupFiles)
    }
    val newFilename = generateFilename()
    fileSaver.saveDatabase(backupFolder, newFilename, database)
    preferences.saveBackupMetadata(journal.getChangeCount(), timestampProvider.now())
  }
  
  private fun generateFilename(): String {
    return "file__${dateTimeFormatter.format(timestampProvider.now())}.kdbx"
  }
  
  private suspend fun removeAllOlderFiles(backupFiles: List<BackupFileData>) {
    val extraFiles = backupFiles.sortedBy { it.lastModified }
        .take(backupFiles.size - THRESHOLD_FILES_COUNT + 1)
    fileSaver.deleteAll(extraFiles)
  }
  
  companion object {
    
    const val THRESHOLD_FILES_COUNT = 20
    
    const val THRESHOLD_DATABASE_CHANGES = 5
    
    val THRESHOLD_PASSED_TIME_SINCE_LAST_BACKUP = TimeUnit.DAYS.toMillis(7)
  }
}
