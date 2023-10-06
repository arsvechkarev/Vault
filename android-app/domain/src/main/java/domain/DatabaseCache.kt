package domain

import app.keemobile.kotpass.database.KeePassDatabase

interface DatabaseCache {
  
  fun save(database: KeePassDatabase)
}
