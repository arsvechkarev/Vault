package com.arsvechkarev.vault.features.settings

import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.HIDDEN

sealed interface SettingsEvent {
  class ShowBiometricsAvailable(val available: Boolean) : SettingsEvent
  class ShowUsernamesReceived(val showUsernames: Boolean) : SettingsEvent
  object MasterPasswordChanged : SettingsEvent
  object ImagesCacheCleared : SettingsEvent
}

sealed interface SettingsUiEvent : SettingsEvent {
  object OnInit : SettingsUiEvent
  object OnHideEnterPasswordDialog : SettingsUiEvent
  object OnChangeMasterPasswordClicked : SettingsUiEvent
  object OnEnteredPasswordToChangeMasterPassword : SettingsUiEvent
  class OnShowUsernamesChanged(val showUsernames: Boolean) : SettingsUiEvent
  class OnEnableBiometricsChanged(val enable: Boolean) : SettingsUiEvent
  object OnClearImagesCacheClicked : SettingsUiEvent
  object OnBackPressed : SettingsUiEvent
}

sealed interface SettingsCommand {
  
  object FetchData : SettingsCommand
  class ChangeShowUsernames(val show: Boolean) : SettingsCommand
  object ClearImagesCache : SettingsCommand
  
  sealed interface RouterCommand : SettingsCommand {
    object GoBack : RouterCommand
    object GoToMasterPasswordScreen : RouterCommand
  }
}

sealed interface SettingsNews {
  class SetShowUsernames(val showUsernames: Boolean) : SettingsNews
  class SetBiometricsEnabled(val enabled: Boolean) : SettingsNews
  object ShowImagesCacheCleared : SettingsNews
  object ShowMasterPasswordChanged : SettingsNews
}

data class SettingsState(
  val showUsernames: Boolean = false,
  val biometricsAvailable: Boolean = false,
  val enterPasswordDialogState: EnterPasswordDialogState = HIDDEN
)

enum class EnterPasswordDialogState {
  SHOWN, HIDDEN, HIDDEN_KEEPING_KEYBOARD
}