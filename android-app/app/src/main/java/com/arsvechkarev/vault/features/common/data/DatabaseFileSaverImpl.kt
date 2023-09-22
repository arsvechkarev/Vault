package com.arsvechkarev.vault.features.common.data

import android.content.Context
import androidx.core.util.AtomicFile
import app.keemobile.kotpass.database.Credentials
import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.decode
import app.keemobile.kotpass.database.encode
import buisnesslogic.DatabaseFileSaver
import buisnesslogic.Password
import buisnesslogic.from
import com.arsvechkarev.vault.core.DispatchersFacade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class DatabaseFileSaverImpl(
  private val filename: String,
  private val context: Context,
  private val dispatchersFacade: DispatchersFacade,
) : DatabaseFileSaver {
  
  private val scope = CoroutineScope(dispatchersFacade.IO)
  private val lock = ReentrantReadWriteLock()
  
  override fun doesDatabaseExist(): Boolean {
    return File(context.filesDir, filename).exists()
  }
  
  override fun save(database: KeePassDatabase) {
    scope.launch { performDatabaseSave(database) }
  }
  
  override suspend fun saveSynchronously(database: KeePassDatabase) {
    withContext(dispatchersFacade.IO) { performDatabaseSave(database) }
  }
  
  override suspend fun read(masterPassword: Password): KeePassDatabase? {
    return withContext(dispatchersFacade.IO) {
      lock.read {
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
  }
  
  private fun performDatabaseSave(database: KeePassDatabase) {
    lock.write {
      val atomicFile = AtomicFile(File(context.filesDir, filename))
      // TODO (9/21/23):
      // Atomic file documentation states that the returned output stream should not be
      // closed manually, it should be passed as is to [atomicFile.finishWrite]. However,
      // database's [encode] function does close the stream under the hood. For now it seems
      // to be working fine, but in case something breaks, kotpass library should probably
      // be forked and the option to leave stream unhandled should be added.
      val outputStream = atomicFile.startWrite()
      database.encode(outputStream)
      atomicFile.finishWrite(outputStream)
    }
  }
}
