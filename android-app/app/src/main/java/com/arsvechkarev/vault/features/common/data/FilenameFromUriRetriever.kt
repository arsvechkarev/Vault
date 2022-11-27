package com.arsvechkarev.vault.features.common.data

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns


interface FilenameFromUriRetriever {
  fun getFilename(uri: Uri, fallback: String): String
}

class FilenameFromUriRetrieverImpl(
  private val context: Context
) : FilenameFromUriRetriever {
  
  override fun getFilename(uri: Uri, fallback: String): String {
    if (uri.scheme == "content") {
      context.contentResolver.query(uri, null, null, null, null).use { cursor ->
        if (cursor != null && cursor.moveToFirst()) {
          val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
          if (columnIndex == -1) {
            return fallback
          }
          return cursor.getString(columnIndex)
        }
      }
    }
    return uri.path?.substringAfterLast("/") ?: fallback
  }
}

