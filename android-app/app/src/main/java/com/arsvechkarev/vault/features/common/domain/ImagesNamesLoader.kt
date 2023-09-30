package com.arsvechkarev.vault.features.common.domain

import com.arsvechkarev.vault.features.common.data.Preferences
import com.arsvechkarev.vault.features.common.data.network.NetworkUrlLoader
import java.util.concurrent.TimeUnit

private const val BASE_URL_ICON_NAMES = "https://raw.githubusercontent.com/arsvechkarev/Vault/master/icons/names.txt"

class ImagesNamesLoader(
  private val networkUrlLoader: NetworkUrlLoader,
  private val preferences: Preferences,
  private val timestampProvider: TimestampProvider
) {
  
  private var cachedNames: Set<String>? = null
  
  suspend fun getFromCacheIfExists(): Set<String>? {
    val lastLoadedTime = preferences.getLong(KEY_LAST_LOADED_TIMESTAMP)
    if (lastLoadedTime == -1L) {
      // No saved timestamp, network has never been loaded, return null
      return null
    }
    if (timestampProvider.now() - lastLoadedTime > TimeUnit.DAYS.toMillis(3)) {
      // More than three days passed since last load, it is better to reload data now
      return null
    }
    return cachedNames ?: preferences.getStringSet(KEY_IMAGES_NAMES)?.also { cachedNames = it }
  }
  
  suspend fun loadFromNetwork(): Result<Set<String>> {
    val result = networkUrlLoader.loadUrl(BASE_URL_ICON_NAMES).map { it.split("\n") }
    return result
        .map(List<String>::toSet)
        .onSuccess { names ->
          preferences.saveAll(mapOf(
            KEY_IMAGES_NAMES to names,
            KEY_LAST_LOADED_TIMESTAMP to timestampProvider.now()
          ))
        }
  }
  
  companion object {
    
    const val KEY_IMAGES_NAMES = "images_names"
    const val KEY_LAST_LOADED_TIMESTAMP = "last_loaded_timestamp"
  }
}
