package com.arsvechkarev.vault.features.creating_password_entry

import buisnesslogic.Password
import buisnesslogic.model.PasswordEntryData

sealed interface CreatingPasswordEntryEvent {
  class PasswordEntryCreated(val passwordId: String) : CreatingPasswordEntryEvent
}

sealed interface CreatingPasswordEntryUiEvent : CreatingPasswordEntryEvent {
  class PasswordEntered(val password: Password) : CreatingPasswordEntryUiEvent
  class OnTitleTextChanged(val text: String) : CreatingPasswordEntryUiEvent
  class OnUsernameTextChanged(val text: String) : CreatingPasswordEntryUiEvent
  object OnContinueClicked : CreatingPasswordEntryUiEvent
  object OnBackPressed : CreatingPasswordEntryUiEvent
}

sealed interface CreatingPasswordEntryCommand {
  
  class SavePassword(val passwordEntryData: PasswordEntryData) : CreatingPasswordEntryCommand
  
  sealed interface RouterCommand : CreatingPasswordEntryCommand {
    object GoToCreatePasswordScreen : RouterCommand
    class GoToInfoScreen(val passwordId: String) : RouterCommand
    object GoBack : RouterCommand
  }
}

data class CreatingPasswordEntryState(
  val title: String = "",
  val username: String = "",
  val showTitleEmptyError: Boolean = false,
)
