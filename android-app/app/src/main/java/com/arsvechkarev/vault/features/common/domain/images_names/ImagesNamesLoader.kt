package com.arsvechkarev.vault.features.common.domain.images_names

import com.arsvechkarev.vault.features.common.data.network.NetworkUrlLoader
import com.arsvechkarev.vault.features.common.data.preferences.Preferences
import com.arsvechkarev.vault.features.common.domain.TimestampProvider
import java.util.concurrent.TimeUnit

private const val BASE_URL_ICON_NAMES = "https://raw.githubusercontent.com/arsvechkarev/Vault/master/icons/names.txt"

class ImagesNamesLoader(
  private val networkUrlLoader: NetworkUrlLoader,
  private val preferences: Preferences,
  private val timestampProvider: TimestampProvider
) {
  
  private var cachedNames: List<ImageNameData>? = null
  
  suspend fun getFromCacheIfExists(): List<ImageNameData>? {
    val lastLoadedTime = preferences.getLong(KEY_LAST_LOADED_TIMESTAMP)
    if (lastLoadedTime == 0L) {
      // No saved timestamp, network has never been loaded, return null
      return null
    }
    if (timestampProvider.now() - lastLoadedTime > TimeUnit.DAYS.toMillis(3)) {
      // More than three days passed since last load, it is better to reload data now
      return null
    }
    if (cachedNames != null) {
      return cachedNames
    }
    val string = preferences.getString(KEY_IMAGES_NAMES) ?: return null
    return ImageNameData.parse(string).also { cachedNames = it }
  }
  
  suspend fun loadFromNetwork(): Result<List<ImageNameData>> {
    return networkUrlLoader.loadUrl(BASE_URL_ICON_NAMES)
        .map(ImageNameData::parse)
        .onSuccess { names ->
          preferences.putAll(
            mapOf(
              KEY_IMAGES_NAMES to ImageNameData.toString(names),
              KEY_LAST_LOADED_TIMESTAMP to timestampProvider.now()
            )
          )
        }
  }
  
  suspend fun clear() {
    cachedNames = null
    preferences.clear()
  }
  
  companion object {
    
    const val KEY_IMAGES_NAMES = "images_names"
    const val KEY_LAST_LOADED_TIMESTAMP = "last_loaded_timestamp"
  }
}