package com.arsvechkarev.vault.features.password_entry.actors

import com.arsvechkarev.vault.core.communicators.Communicator
import com.arsvechkarev.vault.core.extensions.emptyMap
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.PasswordConfigurationMode.EditPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordSendEvent
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.RouterCommand.GoToCreatePasswordScreen
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

class OpenEditPasswordScreenCommunicatorActor(
  private val communicator: Communicator<CreatingPasswordReceiveEvent, CreatingPasswordSendEvent>
) : Actor<PasswordEntryCommand, PasswordEntryEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<PasswordEntryCommand>): Flow<PasswordEntryEvent> {
    return commands.filterIsInstance<GoToCreatePasswordScreen>()
        .emptyMap {
          communicator.input.emit(CreatingPasswordReceiveEvent(EditPassword(it.password)))
        }
  }
}
