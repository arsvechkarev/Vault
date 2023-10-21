package com.arsvechkarev.vault.stubs

import app.keemobile.kotpass.database.Credentials
import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.decode
import app.keemobile.kotpass.database.encode
import domain.DatabaseFileSaver
import domain.Password
import domain.from
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class StubDatabaseFileSaver : DatabaseFileSaver {
  
  private var data: ByteArray? = null
  
  override fun doesDatabaseExist(): Boolean {
    return data != null
  }
  
  override fun save(database: KeePassDatabase) {
    val stream = ByteArrayOutputStream()
    database.encode(stream)
    this.data = stream.toByteArray()
  }
  
  override suspend fun saveSynchronously(database: KeePassDatabase) {
    val stream = ByteArrayOutputStream()
    database.encode(stream)
    this.data = stream.toByteArray()
  }
  
  override suspend fun read(masterPassword: Password): KeePassDatabase {
    return KeePassDatabase.decode(ByteArrayInputStream(data), Credentials.from(masterPassword))
  }
}
