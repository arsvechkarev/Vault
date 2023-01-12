package com.arsvechkarev.vault.features.password_info.actors

import com.arsvechkarev.vault.core.communicators.CommunicatorHolder
import com.arsvechkarev.vault.core.extensions.emptyMap
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.Setup
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.Setup.PasswordConfigurationMode.EditPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordSendEvent
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.OpenEditPasswordScreen
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

class OpenEditPasswordScreenActor(
  private val communicatorHolder: CommunicatorHolder<CreatingPasswordReceiveEvent, CreatingPasswordSendEvent>
) : Actor<PasswordInfoScreenCommand, PasswordInfoScreenEvent> {
  
  override fun handle(commands: Flow<PasswordInfoScreenCommand>): Flow<PasswordInfoScreenEvent> {
    return commands.filterIsInstance<OpenEditPasswordScreen>()
        .emptyMap { communicatorHolder.communicator.input.emit(Setup(EditPassword(it.password))) }
  }
}
