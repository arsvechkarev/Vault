package com.arsvechkarev.vault.features.info.actors

import com.arsvechkarev.vault.features.common.data.ListenableCachedPasswordStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.info.InfoScreenCommand
import com.arsvechkarev.vault.features.info.InfoScreenCommand.UpdateItem
import com.arsvechkarev.vault.features.info.InfoScreenCommand.UpdateItem.UpdateLogin
import com.arsvechkarev.vault.features.info.InfoScreenCommand.UpdateItem.UpdateNotes
import com.arsvechkarev.vault.features.info.InfoScreenCommand.UpdateItem.UpdatePassword
import com.arsvechkarev.vault.features.info.InfoScreenCommand.UpdateItem.UpdateWebsiteName
import com.arsvechkarev.vault.features.info.InfoScreenEvent
import com.arsvechkarev.vault.features.info.InfoScreenEvent.UpdatedInfo.UpdatedLogin
import com.arsvechkarev.vault.features.info.InfoScreenEvent.UpdatedInfo.UpdatedNotes
import com.arsvechkarev.vault.features.info.InfoScreenEvent.UpdatedInfo.UpdatedPassword
import com.arsvechkarev.vault.features.info.InfoScreenEvent.UpdatedInfo.UpdatedWebsiteName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class UpdatePasswordInfoActor(
  private val storage: ListenableCachedPasswordStorage,
  private val masterPasswordProvider: MasterPasswordProvider
) : Actor<InfoScreenCommand, InfoScreenEvent> {
  
  override fun handle(commands: Flow<InfoScreenCommand>): Flow<InfoScreenEvent> {
    return commands.filterIsInstance<UpdateItem>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          storage.updatePassword(masterPassword, command.passwordInfoItem)
          when (command) {
            is UpdateWebsiteName -> UpdatedWebsiteName(command.passwordInfoItem)
            is UpdateLogin -> UpdatedLogin(command.passwordInfoItem)
            is UpdatePassword -> UpdatedPassword(command.passwordInfoItem)
            is UpdateNotes -> UpdatedNotes(command.passwordInfoItem)
          }
        }
  }
}
