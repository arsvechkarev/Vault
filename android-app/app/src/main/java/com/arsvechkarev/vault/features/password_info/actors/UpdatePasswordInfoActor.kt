package com.arsvechkarev.vault.features.password_info.actors

import com.arsvechkarev.vault.core.model.toPassword
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.storage.ListenableCachedEntriesStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.UpdateItem
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.UpdateItem.UpdateLogin
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.UpdateItem.UpdateNotes
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.UpdateItem.UpdatePassword
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.UpdateItem.UpdateWebsiteName
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.UpdatedPasswordInfo.UpdatedLogin
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.UpdatedPasswordInfo.UpdatedNotes
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.UpdatedPasswordInfo.UpdatedPassword
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.UpdatedPasswordInfo.UpdatedWebsiteName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class UpdatePasswordInfoActor(
  private val storage: ListenableCachedEntriesStorage,
  private val masterPasswordProvider: MasterPasswordProvider
) : Actor<InfoScreenCommand, PasswordInfoScreenEvent> {
  
  override fun handle(commands: Flow<InfoScreenCommand>): Flow<PasswordInfoScreenEvent> {
    return commands.filterIsInstance<UpdateItem>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          storage.updateEntry(masterPassword, command.passwordItem.toPassword())
          when (command) {
            is UpdateWebsiteName -> UpdatedWebsiteName(command.passwordItem)
            is UpdateLogin -> UpdatedLogin(command.passwordItem)
            is UpdatePassword -> UpdatedPassword(command.passwordItem)
            is UpdateNotes -> UpdatedNotes(command.passwordItem)
          }
        }
  }
}
