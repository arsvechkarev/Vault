package com.arsvechkarev.vault.features.master_password.actors

import buisnesslogic.PasswordInfoChecker
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.master_password.MasterPasswordCommand
import com.arsvechkarev.vault.features.master_password.MasterPasswordCommand.PasswordCommand.CheckPasswordStatus
import com.arsvechkarev.vault.features.master_password.MasterPasswordEvent
import com.arsvechkarev.vault.features.master_password.MasterPasswordEvent.PasswordIsTheSameAsCurrent
import com.arsvechkarev.vault.features.master_password.MasterPasswordEvent.UpdatedPasswordStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class CheckPasswordStatusActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val passwordInfoChecker: PasswordInfoChecker,
) : Actor<MasterPasswordCommand, MasterPasswordEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<MasterPasswordCommand>): Flow<MasterPasswordEvent> {
    return commands.filterIsInstance<CheckPasswordStatus>()
        .mapLatest { command ->
          if (command.password.stringData ==
              masterPasswordProvider.provideMasterPasswordIfSet()?.stringData) {
            PasswordIsTheSameAsCurrent
          } else {
            val passwordStatus = passwordInfoChecker.checkStatus(command.password)
            UpdatedPasswordStatus(passwordStatus)
          }
        }
  }
}
