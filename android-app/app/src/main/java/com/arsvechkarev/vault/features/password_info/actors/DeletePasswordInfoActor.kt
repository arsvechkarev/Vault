package com.arsvechkarev.vault.features.password_info.actors

import app.keemobile.kotpass.database.modifiers.removeEntry
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.storage.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.DeletePasswordEntry
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.DeletedPasswordInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import java.util.UUID

class DeletePasswordInfoActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val storage: ObservableCachedDatabaseStorage,
) : Actor<PasswordInfoScreenCommand, PasswordInfoScreenEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<PasswordInfoScreenCommand>): Flow<PasswordInfoScreenEvent> {
    return commands.filterIsInstance<DeletePasswordEntry>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          val database = storage.getDatabase(masterPassword)
          val newDatabase = database.removeEntry(UUID.fromString(command.passwordId))
          storage.saveDatabase(newDatabase)
          DeletedPasswordInfo
        }
  }
}
