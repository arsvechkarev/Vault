package com.arsvechkarev.vault.features.creating_entry.actors

import com.arsvechkarev.vault.core.ListenableCachedPasswordStorage
import com.arsvechkarev.vault.core.MasterPasswordProvider
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand.SaveEntry
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent.EntryCreated
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class SavingEntryActor(
  private val storage: ListenableCachedPasswordStorage,
  private val masterPasswordProvider: MasterPasswordProvider,
) : Actor<CreatingEntryCommand, CreatingEntryEvent> {
  
  override fun handle(commands: Flow<CreatingEntryCommand>): Flow<CreatingEntryEvent> {
    return commands.filterIsInstance<SaveEntry>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          val passwordInfoItem = storage.savePassword(
            masterPassword, command.websiteName, command.login, command.password
          )
          EntryCreated(passwordInfoItem)
        }
  }
}