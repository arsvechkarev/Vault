package com.arsvechkarev.vault.test.core.di.stubs

import android.net.Uri
import com.arsvechkarev.vault.features.common.data.files.ExternalFileReader
import java.io.ByteArrayInputStream
import java.io.InputStream

class StubExternalFileReader(
  private val uriToMatch: String? = null,
  private val bytesToRead: (uri: Uri) -> ByteArray = { ByteArray(0) }
) : ExternalFileReader {
  
  override fun getInputStreamFrom(uri: Uri): InputStream {
    if (uri.toString() == uriToMatch || uriToMatch == null) {
      return ByteArrayInputStream(bytesToRead(uri))
    }
    error("Unexpected url: $uri")
  }
}
