package com.arsvechkarev.vault.test.core.stub

import android.net.Uri
import com.arsvechkarev.vault.features.common.data.ExternalFileReader

class StubExternalFileReader(
  private val uriToMatch: String,
  private val bytesToRead: () -> ByteArray
) : ExternalFileReader {
  
  override fun readFile(uri: Uri): ByteArray {
    if (uri.toString() == uriToMatch) {
      return bytesToRead()
    }
    error("Unexpected url: $uri")
  }
}
