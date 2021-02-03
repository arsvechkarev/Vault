package com.arsvechkarev.vault.cryptography

import android.content.Context
import androidx.security.crypto.EncryptedFile
import com.arsvechkarev.vault.core.Singletons.masterKey
import java.io.File

object EncryptedFileUtils {
  
  private val charset = Charsets.UTF_8
  
  fun saveTextToFile(context: Context, filename: String, text: String) {
    val file = File(context.filesDir, filename)
    file.delete()
    val encryptedFile = getEncryptedFile(context, file)
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
    getEncryptedFile(context, file).openFileInput().use { stream ->
      return String(stream.readBytes(), charset)
    }
  }
  
  private fun getEncryptedFile(context: Context, file: File): EncryptedFile {
    val encryptionScheme = EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    return EncryptedFile.Builder(context, file, masterKey, encryptionScheme).build()
  }
}