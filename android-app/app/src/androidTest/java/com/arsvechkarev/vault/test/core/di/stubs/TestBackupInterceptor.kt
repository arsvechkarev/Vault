package com.arsvechkarev.vault.test.core.di.stubs

import app.keemobile.kotpass.database.KeePassDatabase
import com.arsvechkarev.vault.features.common.domain.BackupInterceptor
import com.arsvechkarev.vault.features.common.model.BackupFileData
import java.util.concurrent.CopyOnWriteArrayList

class TestBackupInterceptor : BackupInterceptor {
  
  val backups = CopyOnWriteArrayList<TestBackup>()
  
  private var timestampCounter = 0L
  
  override fun interceptGetAll(): List<BackupFileData> {
    return backups.map(TestBackup::fileData)
  }
  
  override fun interceptDeleteAll(files: List<BackupFileData>): Boolean {
    backups.removeAll { files.contains(it.fileData) }
    return true
  }
  
  override fun interceptBackup(filename: String, database: KeePassDatabase): Boolean {
    backups.add(TestBackup(BackupFileData(filename, ++timestampCounter), database))
    return true
  }
}

data class TestBackup(
  val fileData: BackupFileData,
  val database: KeePassDatabase
)
