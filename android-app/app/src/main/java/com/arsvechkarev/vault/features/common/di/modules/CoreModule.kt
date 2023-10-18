package com.arsvechkarev.vault.features.common.di.modules

import android.app.Application
import com.arsvechkarev.vault.core.DefaultDispatchersFacade
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.features.common.domain.Clipboard
import com.arsvechkarev.vault.features.common.domain.ClipboardImpl
import com.arsvechkarev.vault.features.common.domain.DefaultTimestampProvider
import com.arsvechkarev.vault.features.common.domain.TimestampProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient

interface CoreModule {
  val application: Application
  val dispatchers: DispatchersFacade
  val globalIOScope: CoroutineScope
  val clipboard: Clipboard
  val okHttpClient: OkHttpClient
  val timestampProvider: TimestampProvider
}

class CoreModuleImpl(override val application: Application) : CoreModule {
  override val dispatchers = DefaultDispatchersFacade
  override val globalIOScope = CoroutineScope(SupervisorJob() + dispatchers.IO)
  override val clipboard = ClipboardImpl(application)
  override val okHttpClient = OkHttpClient()
  override val timestampProvider = DefaultTimestampProvider()
}
