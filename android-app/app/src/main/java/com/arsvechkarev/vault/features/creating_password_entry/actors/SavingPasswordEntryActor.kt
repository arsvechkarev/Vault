package com.arsvechkarev.vault.features.creating_password_entry.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.storage.ListenableCachedEntriesStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.SavePassword
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.PasswordEntryCreated
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class SavingPasswordEntryActor(
  private val storage: ListenableCachedEntriesStorage,
  private val masterPasswordProvider: MasterPasswordProvider,
) : Actor<CreatingPasswordEntryCommand, CreatingPasswordEntryEvent> {
  
  override fun handle(commands: Flow<CreatingPasswordEntryCommand>): Flow<CreatingPasswordEntryEvent> {
    return commands.filterIsInstance<SavePassword>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          val passwordItem = storage.savePassword(
            masterPassword, command.websiteName, command.login, command.password
          )
          PasswordEntryCreated(passwordItem)
        }
  }
}
