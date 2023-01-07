package com.arsvechkarev.vault.features.import_passwords.actors

import buisnesslogic.Cryptography
import buisnesslogic.FileSaver
import buisnesslogic.MasterPasswordHolder
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.FileReader
import com.arsvechkarev.vault.features.common.data.storage.ListenableCachedPasswordsStorage
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsCommand
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsCommand.TryImportPasswords
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsEvent
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsEvent.PasswordsImportFailure
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsEvent.PasswordsImportSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber

class ImportPasswordsActor(
  private val fileReader: FileReader,
  private val cryptography: Cryptography,
  private val fileSaver: FileSaver,
  private val storage: ListenableCachedPasswordsStorage,
) : Actor<ImportPasswordsCommand, ImportPasswordsEvent> {
  
  override fun handle(commands: Flow<ImportPasswordsCommand>): Flow<ImportPasswordsEvent> {
    return commands.filterIsInstance<TryImportPasswords>()
        .mapLatest { command ->
          val currentBytes = checkNotNull(fileSaver.readData())
          val result = runCatching {
            val bytes = fileReader.readFile(command.uri)
            
            // Trying to decrypt data
            cryptography.decryptData(command.password, bytes)
            
            // Saving new bytes to file saver temporarily, this should be done because
            // storage reads from file saver
            // TODO (27.11.2022): Make storage independent of FileSaver ?
            fileSaver.saveData(bytes)
            
            // Decryption is successful, trying to parse passwords to
            // make sure format is valid
            storage.getPasswords(command.password)
            bytes
          }.onSuccess { bytes ->
            fileSaver.saveData(bytes)
            storage.notifySubscribers(command.password, reloadData = true)
            MasterPasswordHolder.setMasterPassword(command.password)
          }.onFailure {
            Timber.e(it)
            fileSaver.saveData(currentBytes)
          }
          if (result.isSuccess) {
            PasswordsImportSuccess
          } else {
            PasswordsImportFailure
          }
        }
  }
}
