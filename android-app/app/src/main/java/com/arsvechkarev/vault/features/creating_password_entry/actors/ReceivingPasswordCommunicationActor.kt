package com.arsvechkarev.vault.features.creating_password_entry.actors

import com.arsvechkarev.vault.core.communicators.Communicator
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordSendEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordSendEvent.OnSavingPassword
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.PasswordEntered
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class ReceivingPasswordCommunicationActor(
  private val communicator: Communicator<CreatingPasswordReceiveEvent, CreatingPasswordSendEvent>
) : Actor<CreatingPasswordEntryCommand, CreatingPasswordEntryEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<CreatingPasswordEntryCommand>): Flow<CreatingPasswordEntryEvent> {
    return communicator.output.mapLatest { event ->
      when (event) {
        is OnSavingPassword -> PasswordEntered(event.password)
      }
    }
  }
}
