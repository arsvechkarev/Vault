package com.arsvechkarev.vault.features.common.data.images

import android.graphics.drawable.Drawable

interface ImagesCache {
  
  suspend fun getImage(key: String): Drawable?
  
  suspend fun saveImage(key: String, drawable: Drawable)
}
