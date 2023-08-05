package com.arsvechkarev.vault.features.change_master_password.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordCommand
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordCommand.CheckPassword
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordEvent
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordEvent.ValidationResult.MasterPasswordIsSameAsCurrent
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordEvent.ValidationResult.Success
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class ValidatePasswordActor(
  private val masterPasswordProvider: MasterPasswordProvider
) : Actor<ChangeMasterPasswordCommand, ChangeMasterPasswordEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<ChangeMasterPasswordCommand>): Flow<ChangeMasterPasswordEvent> {
    return commands.filterIsInstance<CheckPassword>()
        .mapLatest { command ->
          if (command.password == masterPasswordProvider.provideMasterPassword()) {
            MasterPasswordIsSameAsCurrent
          } else {
            Success(command.password)
          }
        }
  }
}
