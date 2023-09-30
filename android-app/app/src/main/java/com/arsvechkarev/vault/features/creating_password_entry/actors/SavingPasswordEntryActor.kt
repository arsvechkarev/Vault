package com.arsvechkarev.vault.features.creating_password_entry.actors

import buisnesslogic.interactors.KeePassPasswordModelInteractor
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.database.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.SavePassword
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.PasswordEntryCreated
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class SavingPasswordEntryActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val storage: ObservableCachedDatabaseStorage,
  private val passwordModelInteractor: KeePassPasswordModelInteractor
) : Actor<CreatingPasswordEntryCommand, CreatingPasswordEntryEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<CreatingPasswordEntryCommand>): Flow<CreatingPasswordEntryEvent> {
    return commands.filterIsInstance<SavePassword>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          val database = storage.getDatabase(masterPassword)
          val databaseUUIDPair = passwordModelInteractor
              .addPassword(database, command.passwordEntryData)
          storage.saveDatabase(databaseUUIDPair.first)
          PasswordEntryCreated(databaseUUIDPair.second)
        }
  }
}
