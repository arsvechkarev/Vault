package com.arsvechkarev.vault.features.common.data.network

import com.arsvechkarev.vault.features.common.domain.ImagesNamesLoader
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * Caching result of [ImagesNamesLoader], so that use doesn't have to load icons every time they
 * open the app
 */
class CacheInterceptor : Interceptor {
  
  override fun intercept(chain: Chain): Response {
    val response: Response = chain.proceed(chain.request())
    val cacheControl: CacheControl = CacheControl.Builder()
        .maxAge(1, TimeUnit.DAYS)
        .build()
    return response.newBuilder()
        .removeHeader("Pragma")
        .removeHeader("Cache-Control")
        .header("Cache-Control", cacheControl.toString())
        .build()
  }
}
