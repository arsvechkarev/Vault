package com.arsvechkarev.vault.features.password_entry.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.database.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.FetchPasswordEntry
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.MasterPasswordNull
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.ReceivedPasswordEntry
import domain.interactors.KeePassPasswordModelInteractor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class FetchPasswordEntryActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val storage: ObservableCachedDatabaseStorage,
  private val passwordModelInteractor: KeePassPasswordModelInteractor,
) : Actor<PasswordEntryCommand, PasswordEntryEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<PasswordEntryCommand>): Flow<PasswordEntryEvent> {
    return commands.filterIsInstance<FetchPasswordEntry>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPasswordIfSet()
              ?: return@mapLatest MasterPasswordNull
          val database = storage.getDatabase(masterPassword)
          val passwordEntry = passwordModelInteractor.getPasswordEntry(database, command.passwordId)
          ReceivedPasswordEntry(passwordEntry)
        }
  }
}