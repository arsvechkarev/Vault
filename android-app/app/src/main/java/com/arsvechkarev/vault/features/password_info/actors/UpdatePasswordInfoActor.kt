package com.arsvechkarev.vault.features.password_info.actors

import buisnesslogic.interactors.KeePassPasswordModelInteractor
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.storage.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.UpdateItem
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.UpdateItem.UpdateNotes
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.UpdateItem.UpdatePassword
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.UpdateItem.UpdateTitle
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.UpdateItem.UpdateUsername
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.UpdatedPasswordInfo.UpdatedNotes
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.UpdatedPasswordInfo.UpdatedPassword
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.UpdatedPasswordInfo.UpdatedTitle
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.UpdatedPasswordInfo.UpdatedUsername
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class UpdatePasswordInfoActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val storage: ObservableCachedDatabaseStorage,
  private val passwordModelInteractor: KeePassPasswordModelInteractor,
) : Actor<PasswordInfoScreenCommand, PasswordInfoScreenEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<PasswordInfoScreenCommand>): Flow<PasswordInfoScreenEvent> {
    return commands.filterIsInstance<UpdateItem>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          val database = storage.getDatabase(masterPassword)
          val newDatabase = passwordModelInteractor.editPassword(database, command.passwordEntry)
          storage.saveDatabase(newDatabase)
          when (command) {
            is UpdateTitle -> UpdatedTitle(command.passwordEntry)
            is UpdateUsername -> UpdatedUsername(command.passwordEntry)
            is UpdatePassword -> UpdatedPassword(command.passwordEntry)
            is UpdateNotes -> UpdatedNotes(command.passwordEntry)
          }
        }
  }
}
