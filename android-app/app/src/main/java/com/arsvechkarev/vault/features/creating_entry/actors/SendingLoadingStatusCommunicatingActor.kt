package com.arsvechkarev.vault.features.creating_entry.actors

import com.arsvechkarev.vault.core.communicators.Communicator
import com.arsvechkarev.vault.core.extensions.emptyMap
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand.NotifyEntryCreated
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.HideLoading
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.ShowLoading
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordSendEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.merge

class SendingLoadingStatusCommunicatingActor(
  private val communicator: Communicator<CreatingPasswordReceiveEvent, CreatingPasswordSendEvent>
) : Actor<CreatingEntryCommand, CreatingEntryEvent> {
  
  override fun handle(commands: Flow<CreatingEntryCommand>): Flow<CreatingEntryEvent> {
    return merge(
      commands.filterIsInstance<CreatingEntryEvent>().emptyMap {
        communicator.input.emit(ShowLoading)
      },
      commands.filterIsInstance<NotifyEntryCreated>().emptyMap {
        communicator.input.emit(HideLoading)
      }
    )
  }
}