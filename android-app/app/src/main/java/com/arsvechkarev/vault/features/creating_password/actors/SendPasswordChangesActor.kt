package com.arsvechkarev.vault.features.creating_password.actors

import com.arsvechkarev.vault.core.extensions.emptyMap
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.domain.PasswordObserver
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand.SendPasswordChangeEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

class SendPasswordChangesActor(
  private val passwordObserver: PasswordObserver
) : Actor<CreatingPasswordCommand, CreatingPasswordEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<CreatingPasswordCommand>): Flow<CreatingPasswordEvent> {
    return commands.filterIsInstance<SendPasswordChangeEvent>()
        .emptyMap { command -> passwordObserver.changePassword(command.password) }
  }
}
