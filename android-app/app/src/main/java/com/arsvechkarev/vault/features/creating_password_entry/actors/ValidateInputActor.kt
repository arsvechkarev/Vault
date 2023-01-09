package com.arsvechkarev.vault.features.creating_password_entry.actors

import com.arsvechkarev.vault.core.communicators.CommunicatorHolder
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.Setup
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.Setup.PasswordConfigurationMode.NewPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordSendEvent
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.ValidateInput
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.SendValidationResult
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.ValidationResult.Fail
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.ValidationResult.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class ValidateInputActor(
  private val communicatorHolder: CommunicatorHolder<CreatingPasswordReceiveEvent, CreatingPasswordSendEvent>
) : Actor<CreatingPasswordEntryCommand, CreatingPasswordEntryEvent> {
  
  override fun handle(commands: Flow<CreatingPasswordEntryCommand>): Flow<CreatingPasswordEntryEvent> {
    return commands.filterIsInstance<ValidateInput>()
        .mapLatest { input ->
          val websiteName = input.websiteName
          val login = input.login
          if (websiteName.isNotBlank() && login.isNotBlank()) {
            communicatorHolder.communicator.input.emit(Setup(NewPassword))
            SendValidationResult(Success)
          } else {
            SendValidationResult(
              Fail(websiteNameEmpty = websiteName.isBlank(), loginEmpty = login.isBlank())
            )
          }
        }
  }
}
