package com.arsvechkarev.vault.test.core.di.stubs

import android.content.Context
import android.net.Uri
import com.arsvechkarev.vault.features.common.data.files.UriPersistedMaker

class StubUriPersistentMaker : UriPersistedMaker {
  
  override fun takePersistableUriPermission(context: Context, uri: Uri) = Unit
}
