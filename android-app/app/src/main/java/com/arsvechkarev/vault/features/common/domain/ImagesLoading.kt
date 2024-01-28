package com.arsvechkarev.vault.features.common.domain

import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import coil.dispose
import coil.imageLoader
import coil.load
import coil.request.CachePolicy
import com.arsvechkarev.vault.MainActivity
import com.arsvechkarev.vault.core.views.drawables.BaseShimmerDrawable
import com.arsvechkarev.vault.core.views.drawables.BaseShimmerDrawable.Companion.setShimmerDrawable
import com.arsvechkarev.vault.core.views.drawables.BaseShimmerDrawable.Companion.stopShimmerDrawable
import com.arsvechkarev.vault.core.views.drawables.LetterInCircleDrawable
import com.arsvechkarev.vault.core.views.drawables.LoadingPlaceholderDrawable
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder
import com.arsvechkarev.vault.features.common.domain.images_names.ImageNameData
import com.arsvechkarev.vault.features.common.domain.images_names.ImageNameData.Basic
import com.arsvechkarev.vault.features.common.domain.images_names.ImageNameData.Compound
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

private const val BASE_URL_ICON_FILES = "https://raw.githubusercontent.com/arsvechkarev/Vault/master/icons/files/"
private const val EXTENSION_PNG = ".png"

fun ImageView.setIconForTitle(title: String, onImageLoadingFailed: () -> Unit) {
  val trimmedTitle = title.trim()
  if (trimmedTitle.isEmpty()) {
    setImageDrawable(null)
    return
  }
  (context as MainActivity).lifecycleScope.launch {
    val coreComponent = CoreComponentHolder.coreComponent
    if (!coreComponent.imagesLoadingEnabledInteractor.isImagesLoadingEnabled()) {
      setLetterInCircleDrawable(trimmedTitle.first().toString())
      return@launch
    }
    val imagesNamesLoader = coreComponent.imagesNamesLoader
    val okHttpClient = coreComponent.okHttpClient
    val imagesNames = imagesNamesLoader.getFromCacheIfExists()
    if (imagesNames != null) {
      trySetImageFromMatchingNames(trimmedTitle, imagesNames, okHttpClient, onImageLoadingFailed)
    } else {
      setShimmerDrawable(LoadingPlaceholderDrawable(title.first().toString()))
      imagesNamesLoader.loadFromNetwork()
          .onSuccess { loadedImagesNames ->
            trySetImageFromMatchingNames(trimmedTitle, loadedImagesNames,
              okHttpClient, onImageLoadingFailed)
          }
          .onFailure {
            onImageLoadingFailed()
            setLetterInCircleDrawable(trimmedTitle.first().toString())
          }
    }
  }
}

suspend fun ImageView.trySetImageFromMatchingNames(
  text: String,
  imagesNames: List<ImageNameData>,
  okHttpClient: OkHttpClient,
  onImageLoadingFailed: () -> Unit
) {
  dispose()
  val matchingName = imagesNames.find { imageNameData ->
    when (imageNameData) {
      is Basic -> text.contains(imageNameData.imageName, ignoreCase = true)
      is Compound -> {
        imageNameData.possibleNames.any { possibleName ->
          text.contains(possibleName, ignoreCase = true)
        }
      }
    }
  }
  if (matchingName == null) {
    stopShimmerDrawable()
    setLetterInCircleDrawable(text.first().toString())
    return
  }
  
  val url = BASE_URL_ICON_FILES + matchingName.imageName + EXTENSION_PNG
  
  // Calling this so that we can test images request in ui tests
  CoreComponentHolder.coreComponent.imageRequestsRecorder.recordUrlRequest(id, url)
  
  val imagesCache = CoreComponentHolder.coreComponent.imagesCache
  val cachedImage = imagesCache.getImage(matchingName.imageName)
  if (cachedImage != null) {
    setImageDrawable(cachedImage)
    return
  }
  if (drawable !is BaseShimmerDrawable) {
    setShimmerDrawable(LoadingPlaceholderDrawable(text.first().toString()))
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
        onSuccess = ::setImageDrawable,
        onError = { setLetterInCircleDrawable(text.first().toString()) }
      )
      listener(
        onSuccess = { _, result ->
          (context as? MainActivity?)?.lifecycleScope?.launch {
            imagesCache.saveImage(matchingName.imageName, result.drawable)
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