package domain

import app.keemobile.kotpass.database.KeePassDatabase

interface DatabaseStorage {
  
  suspend fun getDatabase(masterPassword: Password): KeePassDatabase
  
  suspend fun saveDatabaseSynchronously(database: KeePassDatabase)
  
  fun saveDatabase(database: KeePassDatabase)
}
