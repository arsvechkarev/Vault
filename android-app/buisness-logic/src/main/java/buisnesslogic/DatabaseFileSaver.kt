package buisnesslogic

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
   * Saves [database] to the file system
   */
  suspend fun save(database: KeePassDatabase)
  
  /**
   * Returns database from the file system. If the file doesn't exist, returns null
   */
  suspend fun read(masterPassword: Password): KeePassDatabase?
  
  /**
   * Returns uri of the database file. Throws [IllegalStateException] if the file does not exist
   */
  suspend fun getFileUri(): String
}
