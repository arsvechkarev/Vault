package com.arsvechkarev.vault.features.creating_entry

sealed interface CreatingEntryEvent {
  class SendValidationResult(val isSuccessful: Boolean) : CreatingEntryEvent
}

sealed interface CreatingEntryUiEvent : CreatingEntryEvent {
  class OnWebsiteNameTextChanged(val text: String) : CreatingEntryUiEvent
  class OnContinueClicked(val websiteName: String, val login: String) : CreatingEntryUiEvent
  object OnBackButtonClicked : CreatingEntryUiEvent
}

sealed interface CreatingEntryCommand {
  class ValidateInput(val websiteName: String, val login: String) : CreatingEntryCommand
}

data class CreatingEntryState(
  val websiteName: String = "",
  val login: String = "",
)
