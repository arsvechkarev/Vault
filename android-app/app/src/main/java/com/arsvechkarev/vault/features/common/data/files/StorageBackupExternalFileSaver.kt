package com.arsvechkarev.vault.features.common.data.files

import app.keemobile.kotpass.database.KeePassDatabase
import com.arsvechkarev.vault.features.common.model.BackupFileData

interface StorageBackupExternalFileSaver {
  
  suspend fun getAll(directory: String): List<BackupFileData>
  
  suspend fun deleteAll(directory: String, files: List<BackupFileData>)
  
  suspend fun saveDatabase(directory: String, filename: String, database: KeePassDatabase)
}
