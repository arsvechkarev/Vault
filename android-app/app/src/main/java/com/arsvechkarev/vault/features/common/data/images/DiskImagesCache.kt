package com.arsvechkarev.vault.features.common.data.images

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import com.arsvechkarev.vault.core.DispatchersFacade
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class DiskImagesCache(
  private val context: Application,
  private val imagesDirectory: String,
  private val dispatchers: DispatchersFacade
) : ImagesCache {
  
  private val locks = ConcurrentHashMap<String, ReentrantReadWriteLock>()
  
  override suspend fun getImage(key: String): Drawable? = withContext(dispatchers.IO) {
    locks.getOrPut(key) { ReentrantReadWriteLock() }.read {
      val file = File("${context.cacheDir}/$imagesDirectory", key)
      if (!file.exists()) {
        return@withContext null
      }
      val options = BitmapFactory.Options().apply {
        inMutable = false
      }
      return@withContext BitmapFactory.decodeFile(file.path, options).toDrawable(context.resources)
    }
  }
  
  override suspend fun saveImage(key: String, drawable: Drawable) {
    withContext(dispatchers.IO) {
      locks.getOrPut(key) { ReentrantReadWriteLock() }.write {
        val imagesDir = File(context.cacheDir, imagesDirectory)
        imagesDir.mkdirs()
        val file = File(imagesDir, key)
        Files.newOutputStream(file.toPath()).use { outputStream ->
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            drawable.toBitmap().compress(Bitmap.CompressFormat.WEBP_LOSSY, 100, outputStream)
          } else {
            @Suppress("DEPRECATION")
            drawable.toBitmap().compress(Bitmap.CompressFormat.WEBP, 100, outputStream)
          }
        }
      }
    }
  }
  
  override suspend fun clearAll() {
    withContext(dispatchers.IO) {
      locks.entries.forEach { (key, value) ->
        value.write {
          val imagesDir = File(context.cacheDir, imagesDirectory)
          if (!imagesDir.exists()) {
            return@withContext
          }
          File(imagesDir, key).delete()
        }
      }
    }
  }
}