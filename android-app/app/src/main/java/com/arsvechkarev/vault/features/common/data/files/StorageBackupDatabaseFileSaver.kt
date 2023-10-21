package com.arsvechkarev.vault.features.common.data.files

import app.keemobile.kotpass.database.KeePassDatabase
import com.arsvechkarev.vault.features.common.domain.DatabaseChangesJournal
import com.arsvechkarev.vault.features.common.domain.StorageBackupInteractor
import domain.DatabaseFileSaver
import domain.Password
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class StorageBackupDatabaseFileSaver(
  private val databaseFileSaver: DatabaseFileSaver,
  private val databaseChangesJournal: DatabaseChangesJournal,
  private val storageBackupInteractor: StorageBackupInteractor,
  private val scope: CoroutineScope
) : DatabaseFileSaver {
  
  override fun doesDatabaseExist(): Boolean {
    return databaseFileSaver.doesDatabaseExist()
  }
  
  override fun save(database: KeePassDatabase) {
    databaseFileSaver.save(database)
    scope.launch { markChangeAndTryBackup(database) }
  }
  
  override suspend fun saveSynchronously(database: KeePassDatabase) {
    databaseFileSaver.saveSynchronously(database)
    markChangeAndTryBackup(database)
  }
  
  override suspend fun read(masterPassword: Password): KeePassDatabase? {
    return databaseFileSaver.read(masterPassword)?.also { database ->
      storageBackupInteractor.performBackupIfNeeded(database)
    }
  }
  
  private suspend fun markChangeAndTryBackup(database: KeePassDatabase) {
    databaseChangesJournal.markChange()
    storageBackupInteractor.performBackupIfNeeded(database)
  }
}