package com.arsvechkarev.vault.features.creating_master_password.actors

import buisnesslogic.PasswordInfoChecker
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_master_password.CMPCommands
import com.arsvechkarev.vault.features.creating_master_password.CMPEvents
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommands.PasswordCommand
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommands.PasswordCommand.CheckPasswordForErrors
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommands.PasswordCommand.CheckPasswordStrength
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.UpdatePasswordError
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.UpdatePasswordStrength
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class PasswordCheckingActor(
  private val passwordInfoChecker: PasswordInfoChecker,
) : Actor<CMPCommands, CMPEvents> {
  
  override fun handle(
    commands: Flow<CMPCommands>
  ): Flow<CMPEvents> {
    return commands.filterIsInstance<PasswordCommand>()
        .mapLatest { command ->
          when (command) {
            is CheckPasswordStrength -> {
              UpdatePasswordStrength(passwordInfoChecker.checkStrength(command.password))
            }
            is CheckPasswordForErrors -> {
              UpdatePasswordError(passwordInfoChecker.checkForErrors(command.password))
            }
          }
        }
  }
}
