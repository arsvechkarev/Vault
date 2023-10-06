package domain

import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.findEntries
import java.util.UUID

interface UniqueIdProvider {
  fun generateUniqueId(database: KeePassDatabase): UUID
}

class UniqueIdProvideImpl(private val idGenerator: IdGenerator) : UniqueIdProvider {
  
  override fun generateUniqueId(database: KeePassDatabase): UUID {
    while (true) {
      val id = idGenerator.generateRandomId()
      if (database.findEntries { it.uuid == id }.isNotEmpty()) {
        continue
      }
      return id
    }
  }
}
