package com.arsvechkarev.vault.features.info.actors

import com.arsvechkarev.vault.core.communicators.CommunicatorHolder
import com.arsvechkarev.vault.core.extensions.emptyMap
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.Setup
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.Setup.PasswordConfigurationMode.EditPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordSendEvent
import com.arsvechkarev.vault.features.info.InfoScreenCommand
import com.arsvechkarev.vault.features.info.InfoScreenCommand.OpenEditPasswordScreen
import com.arsvechkarev.vault.features.info.InfoScreenEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

class OpenEditPasswordScreenActor(
  private val communicatorHolder: CommunicatorHolder<CreatingPasswordReceiveEvent, CreatingPasswordSendEvent>
) : Actor<InfoScreenCommand, InfoScreenEvent> {
  
  override fun handle(commands: Flow<InfoScreenCommand>): Flow<InfoScreenEvent> {
    return commands.filterIsInstance<OpenEditPasswordScreen>()
        .emptyMap { communicatorHolder.communicator.input.emit(Setup(EditPassword(it.password))) }
  }
}
