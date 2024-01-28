package com.arsvechkarev.vault.features.common.domain

import com.arsvechkarev.vault.features.common.data.images.ImagesCache
import com.arsvechkarev.vault.features.common.data.preferences.Preferences
import com.arsvechkarev.vault.features.common.domain.images_names.ImagesNamesLoader

class ImagesLoadingEnabledInteractor(
  private val imagesCache: ImagesCache,
  private val imagesNamesLoader: ImagesNamesLoader,
  private val reloadImagesObserver: ReloadImagesObserver,
  private val preferences: Preferences,
) {
  
  suspend fun isImagesLoadingEnabled(): Boolean {
    return preferences.getBoolean(KEY, defaultValue = true)
  }
  
  suspend fun setImagesLoadingEnabled(value: Boolean) {
    preferences.putBoolean(KEY, value)
    if (!value) {
      imagesCache.clearAll()
      imagesNamesLoader.clear()
    }
    reloadImagesObserver.requestReload()
  }
  
  companion object {
    
    const val KEY = "enable_images_loading"
  }
}
