package com.arsvechkarev.vault.features.common.data.files

import android.content.Context
import android.net.Uri
import java.io.InputStream

interface ExternalFileReader {
  
  fun getInputStreamFrom(uri: Uri): InputStream
}

class ContextExternalFileReader(
  private val context: Context
) : ExternalFileReader {
  
  override fun getInputStreamFrom(uri: Uri): InputStream {
    return checkNotNull(context.contentResolver.openInputStream(uri))
  }
}
