package com.arsvechkarev.vault.features.creating_password.actors

import com.arsvechkarev.vault.core.communicators.Communicator
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.Setup
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordSendEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReceivingInputCreatingPasswordActor(
  private val communicator: Communicator<CreatingPasswordReceiveEvent, CreatingPasswordSendEvent>
) : Actor<CreatingPasswordCommand, CreatingPasswordEvent> {
  
  override fun handle(commands: Flow<CreatingPasswordCommand>): Flow<CreatingPasswordEvent> {
    return communicator.input.map { event ->
      when (event) {
        is Setup -> CreatingPasswordEvent.Setup(event.mode)
      }
    }
  }
}
