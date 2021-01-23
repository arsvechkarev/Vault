package com.arsvechkarev.vault.core

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import java.io.File

object FileUtils {
  
  private val charset = Charsets.UTF_8
  
  fun saveTextToFile(context: Context, filename: String, text: String) {
    val encryptedFile = getEncryptedFile(context, filename)
    encryptedFile.openFileOutput().use { stream ->
      val bytes = text.toByteArray(charset)
      stream.write(bytes)
    }
  }
  
  fun readTextFromFile(context: Context, filename: String): String {
    val file = context.getFileStreamPath(filename)
    if (!file.exists()) {
      return ""
    }
    getEncryptedFile(context, filename).openFileInput().use { stream ->
      return String(stream.readBytes(), charset)
    }
  }
  
  private fun getEncryptedFile(context: Context, filename: String): EncryptedFile {
    val mainKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    val file = File(context.filesDir, filename)
    val encryptionScheme = EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    return EncryptedFile.Builder(context, file, mainKey, encryptionScheme).build()
  }
}