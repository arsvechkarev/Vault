package com.arsvechkarev.vault.features.common.navigation.result_contracts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile

class SelectFolder : ActivityResultContracts.OpenDocumentTree() {
  
  override fun createIntent(context: Context, input: Uri?): Intent {
    return super.createIntent(context, input).apply {
      if (input != null) {
        DocumentFile.fromTreeUri(context, input)?.let { file ->
          putExtra(DocumentsContract.EXTRA_INITIAL_URI, file.uri)
        }
      }
      addFlags(
        Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            or Intent.FLAG_GRANT_READ_URI_PERMISSION
            or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
      )
    }
  }
}
