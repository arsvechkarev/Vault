package com.arsvechkarev.vault.features.common.data

import android.content.Context
import android.net.Uri
import com.arsvechkarev.vault.core.DispatchersFacade
import kotlinx.coroutines.withContext

interface PasswordsFileExporter {
  
  suspend fun writeData(exportingUri: Uri, data: ByteArray)
}

class RealPasswordsFileExporter(
  private val context: Context,
  private val dispatchersFacade: DispatchersFacade
) : PasswordsFileExporter {
  
  override suspend fun writeData(exportingUri: Uri, data: ByteArray) {
    withContext(dispatchersFacade.IO) {
      val outputStream = context.contentResolver.openOutputStream(exportingUri, "wr")
      checkNotNull(outputStream).use { it.write(data) }
    }
  }
}
