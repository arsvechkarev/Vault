package com.arsvechkarev.vault.features.info.actors

import com.arsvechkarev.vault.core.communicators.Communicator
import com.arsvechkarev.vault.core.extensions.emptyMap
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.HideLoading
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.ShowLoading
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordSendEvent
import com.arsvechkarev.vault.features.info.InfoScreenCommand
import com.arsvechkarev.vault.features.info.InfoScreenCommand.HidePasswordLoadingDialog
import com.arsvechkarev.vault.features.info.InfoScreenCommand.ShowPasswordLoadingDialog
import com.arsvechkarev.vault.features.info.InfoScreenEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.merge

class SendingLoadingStatusCommunicatingActor(
  private val communicator: Communicator<CreatingPasswordReceiveEvent, CreatingPasswordSendEvent>
) : Actor<InfoScreenCommand, InfoScreenEvent> {
  
  override fun handle(commands: Flow<InfoScreenCommand>): Flow<InfoScreenEvent> {
    return merge(
      commands.filterIsInstance<ShowPasswordLoadingDialog>().emptyMap {
        communicator.input.emit(ShowLoading)
      },
      commands.filterIsInstance<HidePasswordLoadingDialog>().emptyMap {
        communicator.input.emit(HideLoading)
      }
    )
  }
}
