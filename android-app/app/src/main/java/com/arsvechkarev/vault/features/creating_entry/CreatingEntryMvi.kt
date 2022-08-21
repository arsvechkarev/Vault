package com.arsvechkarev.vault.features.creating_entry

import com.arsvechkarev.vault.core.model.PasswordInfoItem

sealed interface CreatingEntryEvent {
  class PasswordEntered(val password: String) : CreatingEntryEvent
  class EntryCreated(val passwordInfoItem: PasswordInfoItem) : CreatingEntryEvent
  class SendValidationResult(val validationResult: ValidationResult) : CreatingEntryEvent
  
  sealed interface ValidationResult {
    object Success : ValidationResult
    class Fail(val websiteNameEmpty: Boolean, val loginEmpty: Boolean) : ValidationResult
  }
}

sealed interface CreatingEntryUiEvent : CreatingEntryEvent {
  class OnWebsiteNameTextChanged(val text: String) : CreatingEntryUiEvent
  class OnLoginTextChanged(val text: String) : CreatingEntryUiEvent
  class OnContinueClicked(val websiteName: String, val login: String) : CreatingEntryUiEvent
  object OnBackButtonClicked : CreatingEntryUiEvent
}

sealed interface CreatingEntryCommand {
  class ValidateInput(val websiteName: String, val login: String) : CreatingEntryCommand
  
  class SaveEntry(
    val websiteName: String,
    val login: String,
    val password: String
  ) : CreatingEntryCommand
  
  object NotifyEntryCreated : CreatingEntryCommand
  
  class GoToInfoScreen(val passwordInfoItem: PasswordInfoItem) : CreatingEntryCommand
}

data class CreatingEntryState(
  val websiteName: String = "",
  val login: String = "",
  val websiteNameEmpty: Boolean = false,
  val loginEmpty: Boolean = false,
)
