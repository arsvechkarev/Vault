package com.arsvechkarev.vault.features.creating_master_password.actors

import buisnesslogic.PasswordChecker
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_master_password.CMPCommands
import com.arsvechkarev.vault.features.creating_master_password.CMPEvents
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommands.PasswordCommand
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommands.PasswordCommand.CheckPasswordStrength
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommands.PasswordCommand.Validate
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.UpdatePasswordStatus
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.UpdatePasswordStrength
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class PasswordCheckingActor(
  private val passwordChecker: PasswordChecker,
) : Actor<CMPCommands, CMPEvents> {
  
  override fun handle(
    commands: Flow<CMPCommands>
  ): Flow<CMPEvents> {
    return commands.filterIsInstance<PasswordCommand>()
        .mapLatest { command ->
          when (command) {
            is CheckPasswordStrength -> {
              UpdatePasswordStrength(passwordChecker.checkStrength(command.password))
            }
            is Validate -> {
              UpdatePasswordStatus(passwordChecker.getPasswordStatus(command.password))
            }
          }
        }
  }
}
