package com.arsvechkarev.vault.features.common.di.modules

import android.app.Application
import com.arsvechkarev.vault.core.DefaultDispatchersFacade
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.features.common.domain.Clipboard
import com.arsvechkarev.vault.features.common.domain.ClipboardImpl
import com.arsvechkarev.vault.features.common.domain.DefaultTimestampProvider
import com.arsvechkarev.vault.features.common.domain.TimestampProvider
import okhttp3.OkHttpClient

interface CoreModule {
  val application: Application
  val dispatchers: DispatchersFacade
  val clipboard: Clipboard
  val okHttpClient: OkHttpClient
  val timestampProvider: TimestampProvider
}

class CoreModuleImpl(override val application: Application) : CoreModule {
  override val dispatchers = DefaultDispatchersFacade
  override val clipboard = ClipboardImpl(application)
  
  override val okHttpClient = OkHttpClient()
  
  override val timestampProvider = DefaultTimestampProvider()
}
