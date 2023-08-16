package com.arsvechkarev.vault.features.creating_password_entry.actors

import com.arsvechkarev.vault.core.communicators.Communicator
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.PasswordConfigurationMode.NewPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordSendEvent
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.ValidateInput
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.SendValidationResult
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.ValidationResult.Fail
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.ValidationResult.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class ValidateInputActor(
  private val communicator: Communicator<CreatingPasswordReceiveEvent, CreatingPasswordSendEvent>
) : Actor<CreatingPasswordEntryCommand, CreatingPasswordEntryEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<CreatingPasswordEntryCommand>): Flow<CreatingPasswordEntryEvent> {
    return commands.filterIsInstance<ValidateInput>()
        .mapLatest { input ->
          val websiteName = input.websiteName
          val login = input.login
          if (websiteName.isNotBlank() && login.isNotBlank()) {
            communicator.input.emit(CreatingPasswordReceiveEvent(NewPassword))
            SendValidationResult(Success)
          } else {
            SendValidationResult(
              Fail(websiteNameEmpty = websiteName.isBlank(), loginEmpty = login.isBlank())
            )
          }
        }
  }
}
