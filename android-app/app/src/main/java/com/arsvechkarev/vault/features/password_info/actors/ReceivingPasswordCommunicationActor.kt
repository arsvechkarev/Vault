package com.arsvechkarev.vault.features.password_info.actors

import com.arsvechkarev.vault.core.communicators.Communicator
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordSendEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordSendEvent.OnSavingPassword
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.SavePasswordEventReceived
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class ReceivingPasswordCommunicationActor(
  private val communicator: Communicator<CreatingPasswordReceiveEvent, CreatingPasswordSendEvent>
) : Actor<InfoScreenCommand, PasswordInfoScreenEvent> {
  
  override fun handle(commands: Flow<InfoScreenCommand>): Flow<PasswordInfoScreenEvent> {
    return communicator.output.mapLatest { event ->
      when (event) {
        is OnSavingPassword -> SavePasswordEventReceived(event.password)
      }
    }
  }
}
