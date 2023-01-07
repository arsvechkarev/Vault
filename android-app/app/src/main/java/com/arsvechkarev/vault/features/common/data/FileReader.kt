package com.arsvechkarev.vault.features.common.data

import android.content.Context
import android.net.Uri

interface FileReader {
  fun readFile(uri: Uri): ByteArray
}

class ContextFileReader(
  private val context: Context
) : FileReader {
  
  override fun readFile(uri: Uri): ByteArray {
    return context.contentResolver?.openInputStream(uri)?.use { it.readBytes() }
        ?: error("Problem with content resolver")
  }
}
