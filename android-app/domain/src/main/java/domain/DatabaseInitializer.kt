package domain

import app.keemobile.kotpass.database.Credentials
import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.models.Meta

interface DatabaseInitializer {
  suspend fun initializeDatabase(masterPassword: Password)
}

class DefaultDatabaseInitializer(
  private val databaseCache: DatabaseCache,
  private val databaseFileSaver: DatabaseFileSaver
) : DatabaseInitializer {
  
  override suspend fun initializeDatabase(masterPassword: Password) {
    val database: KeePassDatabase = KeePassDatabase.Ver4x.create(
      rootName = CommonConstants.DEFAULT_DATABASE_NAME,
      meta = Meta(),
      credentials = Credentials.from(masterPassword)
    )
    databaseCache.save(database)
    databaseFileSaver.saveSynchronously(database)
  }
}
