package com.arsvechkarev.vault.test.core.stub

import android.net.Uri
import com.arsvechkarev.vault.features.common.data.PasswordsFileExporter

class StubPasswordsFileExporter : PasswordsFileExporter {
  
  var exportingUri: Uri? = null
  var exportedData: ByteArray? = null
  
  override suspend fun writeData(exportingUri: Uri, data: ByteArray) {
    this.exportingUri = exportingUri
    exportedData = data
  }
}
