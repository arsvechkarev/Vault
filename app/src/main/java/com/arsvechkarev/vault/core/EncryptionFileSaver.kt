package com.arsvechkarev.vault.core

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import buisnesslogic.FileSaver
import java.io.File

class EncryptionFileSaver(
  private val filename: String,
  private val context: Context,
  private val masterKey: MasterKey
) : FileSaver {
  
  private val charset = Charsets.UTF_8
  
  override fun saveTextToFile(text: String) {
    val file = File(context.filesDir, filename)
    file.delete()
    val encryptedFile = getEncryptedFile(context, file)
    encryptedFile.openFileOutput().use { stream ->
      val bytes = text.toByteArray(charset)
      stream.write(bytes)
    }
  }
  
  override fun readTextFromFile(): String {
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