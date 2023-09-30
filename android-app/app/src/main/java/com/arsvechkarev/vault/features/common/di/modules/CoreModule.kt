package com.arsvechkarev.vault.features.common.di.modules

import android.app.Application
import com.arsvechkarev.vault.core.DefaultDispatchersFacade
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.features.common.data.network.CacheInterceptor
import com.arsvechkarev.vault.features.common.domain.Clipboard
import com.arsvechkarev.vault.features.common.domain.ClipboardImpl
import com.arsvechkarev.vault.features.common.domain.DefaultTimestampProvider
import com.arsvechkarev.vault.features.common.domain.TimestampProvider
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File

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
  
  override val okHttpClient = OkHttpClient.Builder()
      .addInterceptor(CacheInterceptor())
      .cache(Cache(
        directory = File(application.cacheDir, "okhttp_cache"),
        maxSize = 1L * 1024L * 1024L // 1 MB
      ))
      .build()
  
  override val timestampProvider = DefaultTimestampProvider()
}
