package com.arsvechkarev.vault.features.settings

import com.arsvechkarev.vault.features.common.biometrics.BiometricsCryptography
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEvent
import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.HIDDEN

sealed interface SettingsEvent {
  class ShowUsernamesReceived(val showUsernames: Boolean) : SettingsEvent
  class ShowBiometricsAvailable(val available: Boolean) : SettingsEvent
  class ShowBiometricsEnabled(val enabled: Boolean) : SettingsEvent
  object MasterPasswordChanged : SettingsEvent
  object BiometricsAdded : SettingsEvent
  object ImagesCacheCleared : SettingsEvent
}

sealed interface SettingsUiEvent : SettingsEvent {
  object OnInit : SettingsUiEvent
  object OnChangeMasterPasswordClicked : SettingsUiEvent
  object OnEnteredPasswordToChangeMasterPassword : SettingsUiEvent
  object OnHideEnterPasswordDialog : SettingsUiEvent
  class OnShowUsernamesChanged(val showUsernames: Boolean) : SettingsUiEvent
  class OnEnableBiometricsChanged(val enabled: Boolean) : SettingsUiEvent
  class OnBiometricsEvent(val event: BiometricsEvent) : SettingsUiEvent
  object OnClearImagesCacheClicked : SettingsUiEvent
  object OnBackPressed : SettingsUiEvent
}

sealed interface SettingsCommand {
  
  object FetchData : SettingsCommand
  class ChangeShowUsernames(val show: Boolean) : SettingsCommand
  class EnableBiometrics(val cryptography: BiometricsCryptography) : SettingsCommand
  object DisableBiometrics : SettingsCommand
  object ClearImagesCache : SettingsCommand
  
  sealed interface RouterCommand : SettingsCommand {
    object GoBack : RouterCommand
    object GoToMasterPasswordScreen : RouterCommand
  }
}

sealed interface SettingsNews {
  class SetShowUsernames(val showUsernames: Boolean) : SettingsNews
  class SetBiometricsEnabled(val enabled: Boolean, val animate: Boolean) : SettingsNews
  object ShowMasterPasswordChanged : SettingsNews
  object ShowBiometricsPrompt : SettingsNews
  object ShowBiometricsAdded : SettingsNews
  class ShowBiometricsError(val error: SettingsBiometricsError) : SettingsNews
  object ShowImagesCacheCleared : SettingsNews
}

data class SettingsState(
  val showUsernames: Boolean = false,
  val biometricsAvailable: Boolean = false,
  val biometricsEnabled: Boolean = false,
  val enterPasswordDialogState: EnterPasswordDialogState = HIDDEN
)

enum class EnterPasswordDialogState {
  SHOWN, HIDDEN, HIDDEN_KEEPING_KEYBOARD
}

enum class SettingsBiometricsError {
  LOCKOUT, LOCKOUT_PERMANENT, OTHER
}