package com.arsvechkarev.vault.features.creating_entry.actors

import com.arsvechkarev.vault.core.communicators.CommunicatorHolder
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand.ValidateInput
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent.SendValidationResult
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent.ValidationResult.Fail
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent.ValidationResult.Success
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.Setup
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.Setup.PasswordConfigurationMode.NewPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordSendEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class ValidateInputActor(
  private val communicatorHolder: CommunicatorHolder<CreatingPasswordReceiveEvent, CreatingPasswordSendEvent>
) : Actor<CreatingEntryCommand, CreatingEntryEvent> {
  
  override fun handle(commands: Flow<CreatingEntryCommand>): Flow<CreatingEntryEvent> {
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
