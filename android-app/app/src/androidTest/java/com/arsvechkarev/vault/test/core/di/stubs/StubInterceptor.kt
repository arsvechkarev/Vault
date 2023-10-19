package com.arsvechkarev.vault.test.core.di.stubs

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.InputStream

private const val BASE_URL = "https://raw.githubusercontent.com/arsvechkarev/Vault/master/icons"
const val URL_ICONS_NAMES = "${BASE_URL}/names.txt"
const val URL_IMAGE_GOOGLE = "${BASE_URL}/files/google.png"

class StubInterceptor(
  private val context: Context
) : Interceptor {
  
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val url = request.url
    return when (url.toString()) {
      URL_ICONS_NAMES -> request.imagesNamesResponse()
      URL_IMAGE_GOOGLE -> request.googleImage()
      // Throwing assertion error so that test fails
      else -> throw AssertionError("Unsupported url: ${request.url}")
    }
  }
  
  private fun Request.imagesNamesResponse(): Response {
    return response("google".toResponseBody("text/plain".toMediaType()))
  }
  
  private fun Request.googleImage(): Response {
    val googleImage = context.assets.open("google.png").use(InputStream::readBytes)
    return response(googleImage.toResponseBody("image/png".toMediaType()))
  }
  
  private fun Request.response(body: ResponseBody): Response {
    return Response.Builder()
        .body(body)
        .code(200)
        .protocol(Protocol.HTTP_2)
        .message("")
        .request(this)
        .build()
  }
}
