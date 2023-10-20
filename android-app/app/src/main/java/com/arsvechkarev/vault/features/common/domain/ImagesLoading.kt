package com.arsvechkarev.vault.features.common.domain

import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import coil.dispose
import coil.imageLoader
import coil.load
import coil.request.CachePolicy
import com.arsvechkarev.vault.MainActivity
import com.arsvechkarev.vault.core.views.drawables.BaseShimmerDrawable.Companion.setShimmerDrawable
import com.arsvechkarev.vault.core.views.drawables.BaseShimmerDrawable.Companion.stopShimmerDrawable
import com.arsvechkarev.vault.core.views.drawables.LetterInCircleDrawable
import com.arsvechkarev.vault.core.views.drawables.LoadingPlaceholderDrawable
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

private const val BASE_URL_ICON_FILES = "https://raw.githubusercontent.com/arsvechkarev/Vault/master/icons/files/"
private const val EXTENSION_PNG = ".png"

fun ImageView.setIconForTitle(title: String, onImageLoadingFailed: () -> Unit) {
  val trimmedText = title.trim()
  if (trimmedText.isEmpty()) {
    setImageDrawable(null)
    return
  }
  val imagesNamesLoader = CoreComponentHolder.coreComponent.imagesNamesLoader
  val okHttpClient = CoreComponentHolder.coreComponent.okHttpClient
  val scope = (context as MainActivity).lifecycleScope
  scope.launch {
    setShimmerDrawable(LoadingPlaceholderDrawable(title.first().toString()))
    val imagesNames = imagesNamesLoader.getFromCacheIfExists()
    if (imagesNames != null) {
      trySetImageFromMatchingNames(trimmedText, imagesNames, okHttpClient, onImageLoadingFailed)
    } else {
      imagesNamesLoader.loadFromNetwork()
          .onSuccess { loadedImagesNames ->
            trySetImageFromMatchingNames(trimmedText, loadedImagesNames,
              okHttpClient, onImageLoadingFailed)
          }
          .onFailure {
            onImageLoadingFailed()
            setLetterInCircleDrawable(trimmedText.first().toString())
          }
    }
  }
}

suspend fun ImageView.trySetImageFromMatchingNames(
  text: String,
  imagesNames: Set<String>,
  okHttpClient: OkHttpClient,
  onImageLoadingFailed: () -> Unit
) {
  dispose()
  val matchingName = imagesNames.find { name -> text.contains(name, ignoreCase = true) }
  if (matchingName == null) {
    stopShimmerDrawable()
    setLetterInCircleDrawable(text.first().toString())
    return
  }
  
  val url = BASE_URL_ICON_FILES + matchingName + EXTENSION_PNG
  
  // Calling this so that we can test images request in ui tests
  CoreComponentHolder.coreComponent.imageRequestsRecorder.recordUrlRequest(id, url)
  
  val imagesCache = CoreComponentHolder.coreComponent.imagesCache
  val cachedImage = imagesCache.getImage(matchingName)
  if (cachedImage != null) {
    setImageDrawable(cachedImage)
    return
  }
  load(
    data = url,
    imageLoader = context.imageLoader.newBuilder().okHttpClient(okHttpClient).build(),
    builder = {
      crossfade(true)
      memoryCachePolicy(CachePolicy.DISABLED)
      diskCachePolicy(CachePolicy.DISABLED)
      error(LetterInCircleDrawable(text.first().toString()))
      target(
        onStart = {
          setShimmerDrawable(LoadingPlaceholderDrawable(text.first().toString()))
        }, onSuccess = { drawable ->
          setImageDrawable(drawable)
        }, onError = {
          setLetterInCircleDrawable(text.first().toString())
        }
      )
      listener(
        onSuccess = { _, result ->
          (context as? MainActivity?)?.lifecycleScope?.launch {
            imagesCache.saveImage(matchingName, result.drawable)
          }
          stopShimmerDrawable()
        },
        onCancel = { stopShimmerDrawable() },
        onError = { _, _ ->
          onImageLoadingFailed()
          stopShimmerDrawable()
        })
    })
}

private fun ImageView.setLetterInCircleDrawable(text: String) {
  val letter = text[0].toString()
  (this.drawable as? LetterInCircleDrawable)?.setLetter(letter) ?: run {
    setImageDrawable(LetterInCircleDrawable(letter))
  }
}