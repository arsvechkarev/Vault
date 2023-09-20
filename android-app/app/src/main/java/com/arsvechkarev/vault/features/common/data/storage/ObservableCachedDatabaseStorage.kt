package com.arsvechkarev.vault.features.common.data.storage

import app.keemobile.kotpass.database.KeePassDatabase
import buisnesslogic.DatabaseStorage
import buisnesslogic.Password
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ObservableCachedDatabaseStorage(
  private val storage: DatabaseStorage
) : DatabaseStorage {
  
  private val _databaseFlow = MutableSharedFlow<KeePassDatabase>()
  val databaseFlow: SharedFlow<KeePassDatabase> get() = _databaseFlow
  
  override suspend fun getDatabase(masterPassword: Password): KeePassDatabase {
    return storage.getDatabase(masterPassword)
  }
  
  override suspend fun saveDatabase(database: KeePassDatabase) {
    storage.saveDatabase(database).also { this._databaseFlow.emit(database) }
  }
}
