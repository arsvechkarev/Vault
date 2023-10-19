package com.arsvechkarev.vault.features.common.data

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.encode
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.features.common.AppConstants.MIME_TYPE_ALL
import com.arsvechkarev.vault.features.common.model.BackupFileData
import kotlinx.coroutines.withContext

class StorageBackupFileSaver(
  private val context: Context,
  private val dispatchers: DispatchersFacade
) {
  
  suspend fun getAll(directory: String): List<BackupFileData> = withContext(dispatchers.IO) {
    val tree = checkNotNull(DocumentFile.fromTreeUri(context, Uri.parse(directory)))
    tree.listFiles().filter { it.name != null }
        .map { BackupFileData(checkNotNull(it.name), it.lastModified()) }
  }
  
  suspend fun deleteAll(
    directory: String,
    files: List<BackupFileData>
  ) = withContext(dispatchers.IO) {
    files.forEach {
      val backupFileUri = getOrCreateBackupFileUri(Uri.parse(directory), it.name)
      DocumentFile.fromSingleUri(context, backupFileUri)?.delete()
    }
  }
  
  @SuppressLint("Recycle")
  suspend fun saveDatabase(
    directory: String,
    filename: String,
    database: KeePassDatabase
  ) = withContext(dispatchers.IO) {
    val newBackupFile = getOrCreateBackupFileUri(Uri.parse(directory), filename)
    context.contentResolver.openOutputStream(newBackupFile)?.use(database::encode)
  }
  
  private fun getOrCreateBackupFileUri(directory: Uri, filename: String): Uri {
    if (directory.scheme == "content") {
      val tree = checkNotNull(DocumentFile.fromTreeUri(context, directory))
      val file = tree.findFile(filename)
          ?: tree.createFile(MIME_TYPE_ALL, filename) ?: error("Failed to create $filename")
      return file.uri
    } else {
      error("Unsupported URI scheme: " + directory.scheme)
    }
  }
}