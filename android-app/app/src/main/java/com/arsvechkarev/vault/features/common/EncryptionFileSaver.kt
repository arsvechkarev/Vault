package com.arsvechkarev.vault.features.common

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

    override fun saveData(data: ByteArray) {
        synchronized(this) {
            val file = File(context.filesDir, filename)
            file.delete()
            val encryptedFile = getEncryptedFile(context, file)
            encryptedFile.openFileOutput().use { stream ->
                stream.write(data)
            }
        }
    }

    override fun readData(): ByteArray? {
        synchronized(this) {
            val file = context.getFileStreamPath(filename)
            if (!file.exists()) {
                return null
            }
            getEncryptedFile(context, file).openFileInput().use { stream ->
                return stream.readBytes()
            }
        }
    }

    override fun delete() {
        synchronized(this) {
            context.getFileStreamPath(filename).delete()
        }
    }

    private fun getEncryptedFile(context: Context, file: File): EncryptedFile {
        val encryptionScheme = EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        return EncryptedFile.Builder(context, file, masterKey, encryptionScheme).build()
    }
}