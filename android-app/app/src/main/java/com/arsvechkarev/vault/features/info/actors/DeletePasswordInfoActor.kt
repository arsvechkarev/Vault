package com.arsvechkarev.vault.features.info.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.storage.ListenableCachedPasswordsStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.info.InfoScreenCommand
import com.arsvechkarev.vault.features.info.InfoScreenCommand.DeletePasswordInfo
import com.arsvechkarev.vault.features.info.InfoScreenEvent
import com.arsvechkarev.vault.features.info.InfoScreenEvent.DeletedPasswordInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class DeletePasswordInfoActor(
  private val storage: ListenableCachedPasswordsStorage,
  private val masterPasswordProvider: MasterPasswordProvider
) : Actor<InfoScreenCommand, InfoScreenEvent> {
  
  override fun handle(commands: Flow<InfoScreenCommand>): Flow<InfoScreenEvent> {
    return commands.filterIsInstance<DeletePasswordInfo>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          storage.deletePassword(masterPassword, command.passwordInfoItem)
          DeletedPasswordInfo
        }
    
  }
}
