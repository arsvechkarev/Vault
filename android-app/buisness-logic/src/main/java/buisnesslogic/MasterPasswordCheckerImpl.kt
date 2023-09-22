package buisnesslogic

import app.keemobile.kotpass.database.Credentials
import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.models.Meta
import java.io.FileNotFoundException

class MasterPasswordCheckerImpl(
  private val databaseFileSaver: DatabaseFileSaver,
  private val databaseCache: DatabaseCache,
) : MasterPasswordChecker {
  
  override suspend fun initializeEncryptedFile(masterPassword: Password) {
    val database: KeePassDatabase = KeePassDatabase.Ver4x.create(
      rootName = DEFAULT_DATABASE_NAME,
      meta = Meta(),
      credentials = Credentials.from(masterPassword)
    )
    databaseCache.save(database)
    databaseFileSaver.saveSynchronously(database)
  }
  
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
      // Error happened during decryption, returning false
      false
    }
  }
}