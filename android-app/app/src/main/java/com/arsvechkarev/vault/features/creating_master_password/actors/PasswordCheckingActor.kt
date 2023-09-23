package com.arsvechkarev.vault.features.creating_master_password.actors

import buisnesslogic.PasswordInfoChecker
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_master_password.CMPCommands
import com.arsvechkarev.vault.features.creating_master_password.CMPEvents
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommand.PasswordCommand
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommand.PasswordCommand.CheckPasswordStatus
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommand.PasswordCommand.CheckPasswordStrength
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.UpdatedPasswordStatus
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.UpdatedPasswordStrength
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class PasswordCheckingActor(
  private val passwordInfoChecker: PasswordInfoChecker,
) : Actor<CMPCommands, CMPEvents> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<CMPCommands>): Flow<CMPEvents> {
    return commands.filterIsInstance<PasswordCommand>()
        .mapLatest { command ->
          when (command) {
            is CheckPasswordStrength -> {
              UpdatedPasswordStrength(passwordInfoChecker.checkStrength(command.password))
            }
            
            is CheckPasswordStatus -> {
              UpdatedPasswordStatus(passwordInfoChecker.checkStatus(command.password))
            }
          }
        }
  }
}
