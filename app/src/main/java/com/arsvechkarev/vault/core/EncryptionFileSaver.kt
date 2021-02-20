package com.arsvechkarev.vault.core

import android.content.Context
import androidx.security.crypto.EncryptedFile
import java.io.File

class EncryptionFileSaver(
  private val context: Context
) : FileSaver {
  
  private val charset = Charsets.UTF_8
  
  override fun saveTextToFile(filename: String, text: String) {
    val file = File(context.filesDir, filename)
    file.delete()
    val encryptedFile = getEncryptedFile(context, file)
    encryptedFile.openFileOutput().use { stream ->
      val bytes = text.toByteArray(charset)
      stream.write(bytes)
    }
  }
  
  override fun readTextFromFile(filename: String): String {
    val file = context.getFileStreamPath(filename)
    if (!file.exists()) {
      return ""
    }
    getEncryptedFile(context, file).openFileInput().use { stream ->
      return String(stream.readBytes(), charset)
    }
  }
  
  private fun getEncryptedFile(context: Context, file: File): EncryptedFile {
    val encryptionScheme = EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    return EncryptedFile.Builder(context, file, Singletons.masterKey, encryptionScheme).build()
  }
}