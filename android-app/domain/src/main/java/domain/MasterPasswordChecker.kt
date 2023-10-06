package domain

import java.io.FileNotFoundException

interface MasterPasswordChecker {
  
  suspend fun isCorrect(masterPassword: Password): Boolean
}

class MasterPasswordCheckerImpl(
  private val databaseFileSaver: DatabaseFileSaver,
  private val databaseCache: DatabaseCache,
) : MasterPasswordChecker {
  
  override suspend fun isCorrect(masterPassword: Password): Boolean {
    return try {
      val database = databaseFileSaver.read(masterPassword) ?: throw FileNotFoundException()
      databaseCache.save(database)
      // Decryption was successful, returning true
      true
    } catch (e: FileNotFoundException) {
      // File was not found for some reason, crashing the app
      throw e
    } catch (e: Throwable) {
      e.printStackTrace()
      // TODO (9/22/23): Add distinction between incorrect password and other types of errors
      // Error happened during decryption, returning false
      false
    }
  }
}
