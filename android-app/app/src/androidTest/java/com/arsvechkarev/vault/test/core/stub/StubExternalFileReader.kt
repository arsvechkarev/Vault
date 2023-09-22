package com.arsvechkarev.vault.test.core.stub

import android.net.Uri
import com.arsvechkarev.vault.features.common.data.ExternalFileReader
import java.io.ByteArrayInputStream
import java.io.InputStream

class StubExternalFileReader(
  private val uriToMatch: String,
  private val bytesToRead: () -> ByteArray
) : ExternalFileReader {
  
  override fun getInputStreamFrom(uri: Uri): InputStream {
    if (uri.toString() == uriToMatch) {
      return ByteArrayInputStream(bytesToRead())
    }
    error("Unexpected url: $uri")
  }
}
