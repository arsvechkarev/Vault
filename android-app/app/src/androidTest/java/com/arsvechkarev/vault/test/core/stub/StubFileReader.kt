package com.arsvechkarev.vault.test.core.stub

import android.net.Uri
import com.arsvechkarev.vault.features.common.data.FileReader

class StubFileReader(
  private val uriToMatch: String,
  private val bytesToRead: () -> ByteArray
) : FileReader {
  
  override fun readFile(uri: Uri): ByteArray {
    if (uri.toString() == uriToMatch) {
      return bytesToRead()
    }
    error("Unexpected url: $uri")
  }
}
