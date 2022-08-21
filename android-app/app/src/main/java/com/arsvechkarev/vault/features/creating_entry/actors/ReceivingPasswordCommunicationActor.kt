package com.arsvechkarev.vault.features.creating_entry.actors

import com.arsvechkarev.vault.core.communicators.Communicator
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent.PasswordEntered
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordSendEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordSendEvent.OnSavingPassword
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class ReceivingPasswordCommunicationActor(
  private val communicator: Communicator<CreatingPasswordReceiveEvent, CreatingPasswordSendEvent>
) : Actor<CreatingEntryCommand, CreatingEntryEvent> {
  
  override fun handle(commands: Flow<CreatingEntryCommand>): Flow<CreatingEntryEvent> {
    return communicator.output.mapLatest { event ->
      when (event) {
        is OnSavingPassword -> PasswordEntered(event.password)
      }
    }
  }
}
