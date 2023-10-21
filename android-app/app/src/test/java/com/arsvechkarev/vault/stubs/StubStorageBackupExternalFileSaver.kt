package com.arsvechkarev.vault.stubs

import app.keemobile.kotpass.database.KeePassDatabase
import com.arsvechkarev.vault.features.common.data.files.StorageBackupExternalFileSaver
import com.arsvechkarev.vault.features.common.model.BackupFileData

class StubStorageBackupExternalFileSaver : StorageBackupExternalFileSaver {
  
  val backups = ArrayList<TestBackup>()
  
  private var timestampCounter = 0L
  
  override suspend fun getAll(directory: String): List<BackupFileData> {
    return backups.map(TestBackup::fileData)
  }
  
  override suspend fun deleteAll(directory: String, files: List<BackupFileData>) {
    backups.removeAll { files.contains(it.fileData) }
  }
  
  override suspend fun saveDatabase(directory: String, filename: String, database: KeePassDatabase) {
    backups.add(TestBackup(BackupFileData(filename, ++timestampCounter), database))
  }
}

data class TestBackup(
  val fileData: BackupFileData,
  val database: KeePassDatabase
)