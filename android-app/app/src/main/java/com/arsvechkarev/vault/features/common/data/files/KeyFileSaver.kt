package com.arsvechkarev.vault.features.common.data.files

import android.content.Context
import com.arsvechkarev.vault.core.DispatchersFacade
import kotlinx.coroutines.withContext
import java.io.File

interface KeyFileSaver {
  
  suspend fun saveKeyFile(bytes: ByteArray)
  
  suspend fun getKeyFileIfExists(): ByteArray?
}

class DefaultKeyFileSaver(
  private val filename: String,
  private val context: Context,
  private val dispatchersFacade: DispatchersFacade,
) : KeyFileSaver {
  
  override suspend fun saveKeyFile(bytes: ByteArray) = withContext(dispatchersFacade.IO) {
    File(context.filesDir, filename).writeBytes(bytes)
  }
  
  override suspend fun getKeyFileIfExists(): ByteArray? = withContext(dispatchersFacade.IO) {
    File(context.filesDir, filename).readBytes()
  }
}
