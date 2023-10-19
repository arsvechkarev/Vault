package com.arsvechkarev.vault.features.common.data.network

import com.arsvechkarev.vault.core.DispatchersFacade
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

interface NetworkUrlLoader {
  
  suspend fun loadUrl(url: String): Result<String>
}

class OkHttpNetworkUrlLoader(
  private val client: OkHttpClient,
  private val dispatchers: DispatchersFacade
) : NetworkUrlLoader {
  
  override suspend fun loadUrl(url: String): Result<String> = withContext(dispatchers.IO) {
    runCatching {
      client.newCall(Request.Builder().url(url).build()).execute().body!!.string()
    }
  }
}