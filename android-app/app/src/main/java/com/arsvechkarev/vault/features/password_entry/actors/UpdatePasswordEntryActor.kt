package com.arsvechkarev.vault.features.password_entry.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.database.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.UpdatePasswordEntry
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.UpdatePasswordEntry.UpdateIsFavorite
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.UpdatePasswordEntry.UpdateNotes
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.UpdatePasswordEntry.UpdatePassword
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.UpdatePasswordEntry.UpdateTitle
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.UpdatePasswordEntry.UpdateUrl
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.UpdatePasswordEntry.UpdateUsername
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.UpdatedPasswordEntry.UpdatedIsFavorite
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.UpdatedPasswordEntry.UpdatedNotes
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.UpdatedPasswordEntry.UpdatedPassword
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.UpdatedPasswordEntry.UpdatedTitle
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.UpdatedPasswordEntry.UpdatedUrl
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.UpdatedPasswordEntry.UpdatedUsername
import domain.interactors.KeePassPasswordEntryInteractor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class UpdatePasswordEntryActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val storage: ObservableCachedDatabaseStorage,
  private val passwordEntryInteractor: KeePassPasswordEntryInteractor,
) : Actor<PasswordEntryCommand, PasswordEntryEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<PasswordEntryCommand>): Flow<PasswordEntryEvent> {
    return commands.filterIsInstance<UpdatePasswordEntry>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          val database = storage.getDatabase(masterPassword)
          val newDatabase = passwordEntryInteractor.editPassword(database, command.passwordEntry)
          storage.saveDatabase(newDatabase)
          when (command) {
            is UpdateTitle -> UpdatedTitle(command.passwordEntry)
            is UpdateUsername -> UpdatedUsername(command.passwordEntry)
            is UpdatePassword -> UpdatedPassword(command.passwordEntry)
            is UpdateUrl -> UpdatedUrl(command.passwordEntry)
            is UpdateNotes -> UpdatedNotes(command.passwordEntry)
            is UpdateIsFavorite -> UpdatedIsFavorite(command.passwordEntry)
          }
        }
  }
}
