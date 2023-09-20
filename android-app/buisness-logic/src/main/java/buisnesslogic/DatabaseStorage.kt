package buisnesslogic

import app.keemobile.kotpass.database.KeePassDatabase

interface DatabaseStorage {
  
  suspend fun getDatabase(masterPassword: Password): KeePassDatabase
  
  suspend fun saveDatabase(database: KeePassDatabase)
}
