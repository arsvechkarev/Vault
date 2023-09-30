package com.arsvechkarev.vault.features.common.data.images

import android.graphics.drawable.Drawable

class MemoryImagesCache(
  private val diskImagesCache: ImagesCache
) : ImagesCache {
  
  private val cache = HashMap<String, Drawable?>()
  
  override suspend fun getImage(key: String): Drawable? {
    return cache.getOrPut(key) { diskImagesCache.getImage(key) }
  }
  
  override suspend fun saveImage(key: String, drawable: Drawable) {
    cache[key] = drawable
    diskImagesCache.saveImage(key, drawable)
  }
  
  override suspend fun clearAll() {
    cache.clear()
    diskImagesCache.clearAll()
  }
}