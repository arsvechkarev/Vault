package domain

import app.keemobile.kotpass.database.KeePassDatabase

/**
 * Database saver that performs I/O operations with database. Implementations should be
 * thread-safe
 */
interface DatabaseFileSaver {
  
  /**
   * Checks whether the database exists
   */
  fun doesDatabaseExist(): Boolean
  
  /**
   * Saves [database] to the file system **asynchronously**
   */
  fun save(database: KeePassDatabase)
  
  /**
   * Saves [database] to the file system synchronously
   */
  suspend fun saveSynchronously(database: KeePassDatabase)
  
  /**
   * Returns database from the file system. If the file doesn't exist, returns null
   */
  suspend fun read(masterPassword: Password): KeePassDatabase?
}
