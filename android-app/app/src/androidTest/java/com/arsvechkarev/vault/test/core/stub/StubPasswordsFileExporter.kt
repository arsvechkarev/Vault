package com.arsvechkarev.vault.test.core.stub

import android.net.Uri
import app.keemobile.kotpass.database.KeePassDatabase
import com.arsvechkarev.vault.features.common.data.files.PasswordsFileExporter

class StubPasswordsFileExporter : PasswordsFileExporter {
  
  var exportingUri: Uri? = null
  var exportedDatabase: KeePassDatabase? = null
  
  override suspend fun writeData(exportingUri: Uri, database: KeePassDatabase) {
    this.exportingUri = exportingUri
    this.exportedDatabase = database
  }
}
