package com.arsvechkarev.vault.core

import android.content.Context


object FileUtils {
  
  private val charset = Charsets.UTF_8
  
  fun saveTextToFile(context: Context, filename: String, text: String) {
    context.openFileOutput(filename, Context.MODE_PRIVATE).use { stream ->
      val bytes = text.toByteArray(charset)
      stream.write(bytes)
    }
  }
  
  fun readTextFromFile(context: Context, filename: String): String {
    val file = context.getFileStreamPath(filename)
    if (!file.exists()) {
      return ""
    }
    context.openFileInput(filename).use { stream ->
      return String(stream.readBytes(), charset)
    }
  }
}