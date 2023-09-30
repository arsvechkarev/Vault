package com.arsvechkarev.vault.features.common.data.network

import com.arsvechkarev.vault.core.DispatchersFacade
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkUrlLoader(
  private val client: OkHttpClient,
  private val dispatchers: DispatchersFacade
) {
  
  suspend fun loadUrl(url: String): Result<String> = withContext(dispatchers.IO) {
    runCatching {
      client.newCall(Request.Builder().url(url).build()).execute().body!!.string()
    }
  }
}