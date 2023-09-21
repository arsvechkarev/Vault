package com.arsvechkarev.vault.features.common.data.storage

import app.keemobile.kotpass.database.KeePassDatabase
import buisnesslogic.DatabaseCache
import buisnesslogic.DatabaseStorage
import buisnesslogic.Password

class CachedDatabaseStorage(
  private val storage: DatabaseStorage
) : DatabaseStorage, DatabaseCache {
  
  @Volatile
  private var database: KeePassDatabase? = null
  
  override suspend fun getDatabase(masterPassword: Password): KeePassDatabase {
    return database ?: storage.getDatabase(masterPassword).also { database = it }
  }
  
  override suspend fun saveDatabase(database: KeePassDatabase) {
    this.database = database
    storage.saveDatabase(database)
  }
  
  override fun save(database: KeePassDatabase) {
    this.database = database
  }
}
