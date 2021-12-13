package buisnesslogic

/**
 * File saver that helps reading and writing text to and from file. Implementations should be
 * thread-safe
 */
interface FileSaver {
  
  /**
   * Saves [text] to file. If file is not created, creates it. If file was not empty prior to
   * this operation, removes old data and saves [text] afterwards
   */
  fun saveTextToFile(text: String)
  
  /**
   * Returns text from file. If file is not created, returns empty string
   */
  fun readTextFromFile(): String
  
  /**
   * Deletes file. If file is already deleted, does nothing
   */
  fun deleteFile()
}