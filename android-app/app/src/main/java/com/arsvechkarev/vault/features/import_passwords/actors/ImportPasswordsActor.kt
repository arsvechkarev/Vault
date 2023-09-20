package com.arsvechkarev.vault.features.import_passwords.actors

import buisnesslogic.DatabaseFileSaver
import buisnesslogic.MasterPasswordHolder
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.ExternalFileReader
import com.arsvechkarev.vault.features.common.data.storage.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsCommand
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsCommand.TryImportPasswords
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsEvent
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsEvent.PasswordsImportFailure
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsEvent.PasswordsImportSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber

class ImportPasswordsActor(
  private val externalFileReader: ExternalFileReader,
  //  private val cryptography: Cryptography,
  private val databaseFileSaver: DatabaseFileSaver,
  private val storage: ObservableCachedDatabaseStorage,
) : Actor<ImportPasswordsCommand, ImportPasswordsEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<ImportPasswordsCommand>): Flow<ImportPasswordsEvent> {
    return commands.filterIsInstance<TryImportPasswords>()
        .mapLatest { command ->
          val currentBytes = databaseFileSaver.read(command.password)
          val result = runCatching {
            val bytes = externalFileReader.readFile(command.uri)
            
            // Trying to decrypt data
            //            cryptography.decryptData(command.password, bytes)
            
            // Saving new bytes to file saver temporarily, this should be done because
            // storage reads from file saver
            // TODO (27.11.2022): Make storage independent of FileSaver ?
            // TODO (9/19/23): Create importing functionality
            //            databaseSaver.save(bytes)
            
            // Decryption is successful, trying to parse passwords to
            // make sure format is valid
            //            storage.getEntries(command.password)
          }.onSuccess {
            //            storage.notifySubscribers(command.password, reloadData = true)
            MasterPasswordHolder.setMasterPassword(command.password)
          }.onFailure {
            Timber.e(it)
            currentBytes?.let { bytes -> databaseFileSaver.save(bytes) }
          }
          if (result.isSuccess) {
            PasswordsImportSuccess
          } else {
            PasswordsImportFailure
          }
        }
  }
}
