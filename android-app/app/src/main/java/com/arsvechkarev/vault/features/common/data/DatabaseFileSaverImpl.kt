package com.arsvechkarev.vault.features.common.data

import android.content.Context
import app.keemobile.kotpass.database.Credentials
import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.decode
import app.keemobile.kotpass.database.encode
import buisnesslogic.DatabaseFileSaver
import buisnesslogic.Password
import buisnesslogic.from
import com.arsvechkarev.vault.core.DispatchersFacade
import kotlinx.coroutines.withContext
import java.io.File

class DatabaseFileSaverImpl(
  private val filename: String,
  private val context: Context,
  private val dispatchersFacade: DispatchersFacade,
) : DatabaseFileSaver {
  
  override fun doesDatabaseExist(): Boolean {
    return File(context.filesDir, filename).exists()
  }
  
  override suspend fun save(database: KeePassDatabase) {
    withContext(dispatchersFacade.IO) {
      val file = File(context.filesDir, filename)
      file.delete()
      database.encode(file.outputStream())
    }
  }
  
  override suspend fun read(masterPassword: Password): KeePassDatabase? {
    return withContext(dispatchersFacade.IO) {
      val file = context.getFileStreamPath(filename)
      if (!file.exists()) {
        return@withContext null
      }
      return@withContext file.inputStream()
          .use { inputStream ->
            KeePassDatabase.decode(inputStream, Credentials.from(masterPassword))
          }
    }
  }
  
  override suspend fun getFileUri(): String {
    val file = File(context.filesDir, filename)
    check(file.exists())
    return file.toURI().toString()
  }
  
  override suspend fun delete(): Unit = withContext(dispatchersFacade.IO) {
    context.getFileStreamPath(filename).delete()
  }
}
