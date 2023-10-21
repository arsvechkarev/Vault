package com.arsvechkarev.vault.features.common.data

import android.content.Context
import android.content.Intent
import android.net.Uri

interface UriPersistedMaker {
  fun takePersistableUriPermission(context: Context, uri: Uri)
}

class ContextResolverUriPersistedMaker : UriPersistedMaker {
  
  override fun takePersistableUriPermission(context: Context, uri: Uri) {
    context.contentResolver?.takePersistableUriPermission(
      uri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    )
  }
}
