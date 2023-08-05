package buisnesslogic

/**
 * File saver that helps to read and to write text to and from file. Implementations should be
 * thread-safe
 */
interface FileSaver {
  
  /**
   * Checks whether the file exists
   */
  fun doesFileExist(): Boolean
  
  /**
   * Saves [data] to the file. If the file is didn't exist, creates it. If file was not empty prior
   * to this operation, removes old data and saves [data] afterward
   */
  suspend fun saveData(data: ByteArray)
  
  /**
   * Returns data from the file. If the file doesn't exist, returns null
   */
  suspend fun readData(): ByteArray?
  
  /**
   * Returns uri of the file. Throws [IllegalStateException] if file does not exist
   */
  suspend fun getFileUri(): String
  
  /**
   * Deletes file. If file is already deleted, does nothing
   */
  suspend fun delete()
}
