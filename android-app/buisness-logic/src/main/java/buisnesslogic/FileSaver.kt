package buisnesslogic

/**
 * File saver that helps to read and to write text to and from file. Implementations should be
 * thread-safe
 */
interface FileSaver {
  
  /**
   * Saves [data] to the file. If the file is didn't exist, creates it. If file was not empty prior
   * to this operation, removes old data and saves [data] afterwards
   */
  suspend fun saveData(data: ByteArray)
  
  /**
   * Returns data from the file. If the file doesn't exist, returns null
   */
  suspend fun readData(): ByteArray?
  
  /**
   * Deletes file. If file is already deleted, does nothing
   */
  suspend fun delete()
}