package buisnesslogic

import app.keemobile.kotpass.database.Credentials
import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.models.Meta
import java.io.FileNotFoundException

class MasterPasswordCheckerImpl(
  private val databaseSaver: DatabaseSaver
) : MasterPasswordChecker {
  
  override suspend fun initializeEncryptedFile(masterPassword: Password) {
    val database: KeePassDatabase = KeePassDatabase.Ver4x.create(
      rootName = DEFAULT_DATABASE_NAME,
      meta = Meta(),
      credentials = Credentials.from(masterPassword)
    )
    databaseSaver.save(database)
  }
  
  override suspend fun isCorrect(masterPassword: Password): Boolean {
    return try {
      databaseSaver.read(masterPassword) ?: throw FileNotFoundException()
      // Decryption was successful, returning true
      true
    } catch (e: FileNotFoundException) {
      // File was not found for some reason, crashing the app
      throw e
    } catch (e: Throwable) {
      // Error happened during decryption, returning false
      false
    }
  }
}