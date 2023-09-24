package com.arsvechkarev.vault.features.settings

import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.HIDDEN

sealed interface SettingsEvent {
  object MasterPasswordChanged : SettingsEvent
}

sealed interface SettingsUiEvent : SettingsEvent {
  object OnHideEnterPasswordDialog : SettingsUiEvent
  object OnChangeMasterPasswordClicked : SettingsUiEvent
  object OnEnteredPasswordToChangeMasterPassword : SettingsUiEvent
  object OnBackPressed : SettingsUiEvent
}

sealed interface SettingsCommand {
  
  sealed interface RouterCommand : SettingsCommand {
    object GoBack : RouterCommand
    object GoToMasterPasswordScreen : RouterCommand
  }
}

sealed interface SettingsNews {
  object ShowMasterPasswordChanged : SettingsNews
}

data class SettingsState(
  val enterPasswordDialogState: EnterPasswordDialogState = HIDDEN
)

enum class EnterPasswordDialogState {
  SHOWN, HIDDEN, HIDDEN_KEEPING_KEYBOARD
}