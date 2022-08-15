package com.arsvechkarev.vault.features.creating_entry.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand.ValidateInput
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent.SendValidationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class ValidateInputActor : Actor<CreatingEntryCommand, CreatingEntryEvent> {
  
  override fun handle(commands: Flow<CreatingEntryCommand>): Flow<CreatingEntryEvent> {
    return commands.filterIsInstance<ValidateInput>()
        .mapLatest { input ->
          SendValidationResult(
            isSuccessful = input.websiteName.isNotBlank() && input.login.isNotBlank()
          )
        }
  }
}