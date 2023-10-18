package com.arsvechkarev.vault.features.common.data

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.encode
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.features.common.model.BackupFileData
import domain.MIME_TYPE_ALL
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class StorageBackupFileSaver(
  private val context: Context,
  private val dispatchers: DispatchersFacade
) {
  
  private val lock = ReentrantReadWriteLock()
  
  suspend fun getAll(directory: String): List<BackupFileData> = withContext(dispatchers.IO) {
    lock.read {
      File(directory).listFiles()?.map { BackupFileData(it.name, it.lastModified()) } ?: emptyList()
    }
  }
  
  suspend fun deleteAll(files: List<BackupFileData>) = withContext(dispatchers.IO) {
    lock.write {
      files.forEach { File(it.name).delete() }
    }
  }
  
  @SuppressLint("Recycle")
  suspend fun saveDatabase(
    directory: String,
    filename: String,
    database: KeePassDatabase
  ) = withContext(dispatchers.IO) {
    lock.write {
      val newBackupFile = newBackupFile(Uri.parse(directory), filename)
      context.contentResolver.openOutputStream(newBackupFile)?.use(database::encode)
    }
  }
  
  private fun newBackupFile(destination: Uri, filename: String): Uri {
    return when (destination.scheme) {
      "content" -> {
        val tree = DocumentFile.fromTreeUri(context, destination)
        val file = tree?.createFile(MIME_TYPE_ALL, filename) ?: error("Failed to create $filename")
        file.uri
      }
      "file" -> {
        val path = destination.path ?: error("No path found in $destination")
        val dir = File(path)
        if (!dir.exists() && !dir.mkdirs()) {
          error("Failed to create ${dir.absolutePath}")
        }
        val file = File(dir.absolutePath, filename)
        if (file.createNewFile()) {
          return Uri.fromFile(file)
        }
        error("Failed to create $filename")
      }
      else -> error("Unknown URI scheme: " + destination.scheme)
    }
  }
}