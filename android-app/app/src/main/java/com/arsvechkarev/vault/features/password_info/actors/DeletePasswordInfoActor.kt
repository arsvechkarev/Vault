package com.arsvechkarev.vault.features.password_info.actors

import com.arsvechkarev.vault.core.model.toPassword
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.storage.ListenableCachedEntriesStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.DeletePasswordInfo
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.DeletedPasswordInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class DeletePasswordInfoActor(
  private val storage: ListenableCachedEntriesStorage,
  private val masterPasswordProvider: MasterPasswordProvider
) : Actor<InfoScreenCommand, PasswordInfoScreenEvent> {
  
  override fun handle(commands: Flow<InfoScreenCommand>): Flow<PasswordInfoScreenEvent> {
    return commands.filterIsInstance<DeletePasswordInfo>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          storage.deleteEntry(masterPassword, command.passwordItem.toPassword())
          DeletedPasswordInfo
        }
    
  }
}
