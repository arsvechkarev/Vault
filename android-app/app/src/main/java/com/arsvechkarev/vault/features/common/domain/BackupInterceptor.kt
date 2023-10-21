package com.arsvechkarev.vault.features.common.domain

import app.keemobile.kotpass.database.KeePassDatabase
import com.arsvechkarev.vault.features.common.model.BackupFileData

interface BackupInterceptor {
  
  fun interceptGetAll(): List<BackupFileData>?
  fun interceptDeleteAll(files: List<BackupFileData>): Boolean
  fun interceptBackup(filename: String, database: KeePassDatabase): Boolean
}

object NoOpBackupInterceptor : BackupInterceptor {
  
  override fun interceptGetAll(): List<BackupFileData>? = null
  override fun interceptDeleteAll(files: List<BackupFileData>) = false
  override fun interceptBackup(filename: String, database: KeePassDatabase) = false
}
