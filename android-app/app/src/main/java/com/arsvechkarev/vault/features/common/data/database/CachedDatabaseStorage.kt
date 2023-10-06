package com.arsvechkarev.vault.features.common.data.database

import app.keemobile.kotpass.database.KeePassDatabase
import domain.DatabaseCache
import domain.DatabaseStorage
import domain.Password

class CachedDatabaseStorage(
  private val storage: DatabaseStorage
) : DatabaseStorage, DatabaseCache {
  
  @Volatile
  private var database: KeePassDatabase? = null
  
  override suspend fun getDatabase(masterPassword: Password): KeePassDatabase {
    return database ?: storage.getDatabase(masterPassword).also { database = it }
  }
  
  override suspend fun saveDatabaseSynchronously(database: KeePassDatabase) {
    this.database = database
    storage.saveDatabaseSynchronously(database)
  }
  
  override fun saveDatabase(database: KeePassDatabase) {
    this.database = database
    storage.saveDatabase(database)
  }
  
  override fun save(database: KeePassDatabase) {
    this.database = database
  }
}
