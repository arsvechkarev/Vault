package com.arsvechkarev.vault.features.creating_password_entry

import com.arsvechkarev.vault.features.common.model.PasswordItem

sealed interface CreatingPasswordEntryEvent {
  class PasswordEntryCreated(val passwordItem: PasswordItem) : CreatingPasswordEntryEvent
  class SendValidationResult(val validationResult: ValidationResult) : CreatingPasswordEntryEvent
  
  sealed interface ValidationResult {
    object Success : ValidationResult
    class Fail(val websiteNameEmpty: Boolean, val loginEmpty: Boolean) : ValidationResult
  }
}

sealed interface CreatingPasswordEntryUiEvent : CreatingPasswordEntryEvent {
  class PasswordEntered(val password: String) : CreatingPasswordEntryUiEvent
  class OnWebsiteNameTextChanged(val text: String) : CreatingPasswordEntryUiEvent
  class OnLoginTextChanged(val text: String) : CreatingPasswordEntryUiEvent
  class OnContinueClicked(val websiteName: String, val login: String) : CreatingPasswordEntryUiEvent
  object OnBackPressed : CreatingPasswordEntryUiEvent
}

sealed interface CreatingPasswordEntryCommand {
  class ValidateInput(val websiteName: String, val login: String) : CreatingPasswordEntryCommand
  
  class SavePassword(
    val websiteName: String,
    val login: String,
    val password: String
  ) : CreatingPasswordEntryCommand
  
  sealed interface RouterCommand : CreatingPasswordEntryCommand {
    object GoToCreatePasswordScreen : RouterCommand
    class GoToInfoScreen(val passwordItem: PasswordItem) : RouterCommand
    object GoBack : RouterCommand
  }
}

data class CreatingPasswordEntryState(
  val websiteName: String = "",
  val login: String = "",
  val websiteNameEmpty: Boolean = false,
  val loginEmpty: Boolean = false,
)
