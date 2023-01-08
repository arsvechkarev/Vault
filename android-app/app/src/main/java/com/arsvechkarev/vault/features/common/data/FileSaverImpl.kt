package com.arsvechkarev.vault.features.common.data

import android.content.Context
import buisnesslogic.FileSaver
import com.arsvechkarev.vault.core.DispatchersFacade
import kotlinx.coroutines.withContext
import java.io.File

class FileSaverImpl(
  private val filename: String,
  private val context: Context,
  private val dispatchersFacade: DispatchersFacade,
) : FileSaver {
  
  override fun doesFileExist(): Boolean {
    return File(context.filesDir, filename).exists()
  }
  
  override suspend fun saveData(data: ByteArray) = withContext(dispatchersFacade.IO) {
    val file = File(context.filesDir, filename)
    file.delete()
    file.writeBytes(data)
  }
  
  override suspend fun readData(): ByteArray? = withContext(dispatchersFacade.IO) {
    val file = context.getFileStreamPath(filename)
    if (!file.exists()) {
      return@withContext null
    }
    return@withContext file.readBytes()
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
