package com.arsvechkarev.vault.features.import_passwords.actors

import app.keemobile.kotpass.database.Credentials
import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.decode
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.features.common.data.database.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.data.files.ExternalFileReader
import com.arsvechkarev.vault.features.common.data.files.KeyFileSaver
import com.arsvechkarev.vault.features.common.domain.StorageBackupInteractor
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsCommand
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsCommand.TryImportPasswords
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsEvent
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsEvent.PasswordsImportFailure
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsEvent.PasswordsImportSuccess
import domain.MasterPasswordHolder
import domain.from
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import okio.use
import timber.log.Timber
import java.io.InputStream

class ImportPasswordsActor(
  private val externalFileReader: ExternalFileReader,
  private val keyFileSaver: KeyFileSaver,
  private val storage: ObservableCachedDatabaseStorage,
  private val backupInteractor: StorageBackupInteractor,
) : Actor<ImportPasswordsCommand, ImportPasswordsEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<ImportPasswordsCommand>): Flow<ImportPasswordsEvent> {
    return commands.filterIsInstance<TryImportPasswords>()
        .mapLatest { command ->
          delay(Durations.StubDelay)
          val result = runCatching {
            val inputStream = externalFileReader.getInputStreamFrom(command.passwordsFileUri)
            val keyData = command.keyFileUri?.let(externalFileReader::getInputStreamFrom)
                ?.use(InputStream::readBytes)?.also { keyFileSaver.saveKeyFile(it) }
            val credentials = Credentials.from(command.password, keyData)
            val newDatabase = KeePassDatabase.decode(inputStream, credentials)
            storage.saveDatabase(newDatabase)
            backupInteractor.forceBackup(newDatabase)
          }.onSuccess {
            MasterPasswordHolder.setMasterPassword(command.password)
          }.onFailure { error ->
            Timber.e(error)
          }
          if (result.isSuccess) {
            PasswordsImportSuccess
          } else {
            PasswordsImportFailure
          }
        }
  }
}
