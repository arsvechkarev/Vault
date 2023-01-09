package com.arsvechkarev.vault.features.common.data

import android.content.Context
import android.net.Uri

interface ExternalFileReader {
  
  fun readFile(uri: Uri): ByteArray
}

class ContextExternalFileReader(
  private val context: Context
) : ExternalFileReader {
  
  override fun readFile(uri: Uri): ByteArray {
    return context.contentResolver?.openInputStream(uri)?.use { it.readBytes() }
        ?: error("Problem with content resolver")
  }
}
