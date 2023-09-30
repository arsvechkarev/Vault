package com.arsvechkarev.vault.features.common.data.database

import app.keemobile.kotpass.database.KeePassDatabase
import buisnesslogic.DatabaseStorage
import buisnesslogic.Password
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ObservableCachedDatabaseStorage(
  private val storage: DatabaseStorage
) : DatabaseStorage {
  
  private val _databaseFlow = MutableSharedFlow<KeePassDatabase>(extraBufferCapacity = 1)
  val databaseFlow: SharedFlow<KeePassDatabase> get() = _databaseFlow
  
  override suspend fun getDatabase(masterPassword: Password): KeePassDatabase {
    return storage.getDatabase(masterPassword)
  }
  
  override fun saveDatabase(database: KeePassDatabase) {
    _databaseFlow.tryEmit(database)
    storage.saveDatabase(database)
  }
  
  override suspend fun saveDatabaseSynchronously(database: KeePassDatabase) {
    _databaseFlow.emit(database)
    storage.saveDatabase(database)
  }
}
