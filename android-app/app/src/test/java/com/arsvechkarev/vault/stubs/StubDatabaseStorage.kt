package com.arsvechkarev.vault.stubs

import app.keemobile.kotpass.database.KeePassDatabase
import domain.DatabaseFileSaver
import domain.DatabaseStorage
import domain.Password

class StubDatabaseStorage(
  private val fileSaver: DatabaseFileSaver
) : DatabaseStorage {
  
  override suspend fun getDatabase(masterPassword: Password): KeePassDatabase {
    return checkNotNull(fileSaver.read(masterPassword))
  }
  
  override suspend fun saveDatabaseSynchronously(database: KeePassDatabase) {
    fileSaver.saveSynchronously(database)
  }
  
  override fun saveDatabase(database: KeePassDatabase) {
    fileSaver.save(database)
  }
}
