package com.arsvechkarev.vault.features.settings

import android.net.Uri
import com.arsvechkarev.vault.features.common.biometrics.BiometricsCryptography
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEvent
import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.HIDDEN

sealed interface SettingsEvent {
  class ReceivedShowUsernames(val showUsernames: Boolean) : SettingsEvent
  class ReceivedBiometricsAvailable(val available: Boolean) : SettingsEvent
  class ReceivedBiometricsEnabled(val enabled: Boolean) : SettingsEvent
  class ReceivedStorageBackupEnabled(val enabled: Boolean, val backupFolderUri: Uri?) :
    SettingsEvent
  
  object MasterPasswordChanged : SettingsEvent
  object BiometricsAdded : SettingsEvent
  class StorageBackupEnabled(val backupFolderUri: Uri) : SettingsEvent
  
  object StorageBackupDisabled : SettingsEvent
  object ImagesCacheCleared : SettingsEvent
}

sealed interface SettingsUiEvent : SettingsEvent {
  object OnInit : SettingsUiEvent
  object OnAppearedOnScreen : SettingsUiEvent
  object OnChangeMasterPasswordClicked : SettingsUiEvent
  object OnEnteredPasswordToChangeMasterPassword : SettingsUiEvent
  object OnHideEnterPasswordDialog : SettingsUiEvent
  class OnShowUsernamesChanged(val showUsernames: Boolean) : SettingsUiEvent
  class OnEnableBiometricsChanged(val enabled: Boolean) : SettingsUiEvent
  class OnBiometricsEvent(val event: BiometricsEvent) : SettingsUiEvent
  class OnEnableStorageBackupChanged(val enabled: Boolean) : SettingsUiEvent
  object OnSelectBackupFolderClicked : SettingsUiEvent
  class OnSelectedBackupFolder(val uri: Uri) : SettingsUiEvent
  object OnClearImagesCacheClicked : SettingsUiEvent
  object OnBackPressed : SettingsUiEvent
}

sealed interface SettingsCommand {
  
  object FetchData : SettingsCommand
  object FetchStorageBackupEnabled : SettingsCommand
  class ChangeShowUsernames(val show: Boolean) : SettingsCommand
  class EnableBiometrics(val cryptography: BiometricsCryptography) : SettingsCommand
  object DisableBiometrics : SettingsCommand
  object ClearImagesCache : SettingsCommand
  
  sealed interface BackupCommand : SettingsCommand {
    class EnableStorageBackup(val backupFolderUri: Uri) : BackupCommand
    object DisableStorageBackup : BackupCommand
  }
  
  sealed interface RouterCommand : SettingsCommand {
    object GoBack : RouterCommand
    object GoToMasterPasswordScreen : RouterCommand
  }
}

sealed interface SettingsNews {
  class SetShowUsernames(val showUsernames: Boolean) : SettingsNews
  class SetBiometricsEnabled(val enabled: Boolean, val animate: Boolean) : SettingsNews
  class SetStorageBackupEnabled(val enabled: Boolean) : SettingsNews
  object ShowMasterPasswordChanged : SettingsNews
  object ShowBiometricsPrompt : SettingsNews
  object ShowBiometricsAdded : SettingsNews
  class ShowBiometricsError(val error: SettingsBiometricsError) : SettingsNews
  class LaunchFolderSelection(val initialUri: Uri?) : SettingsNews
  object ShowStorageBackupEnabled : SettingsNews
  object ShowImagesCacheCleared : SettingsNews
}

data class SettingsState(
  val showUsernames: Boolean = false,
  val biometricsAvailable: Boolean = false,
  val biometricsEnabled: Boolean = false,
  val storageBackupEnabled: Boolean = false,
  val storageBackupFolderUri: Uri? = null,
  val enterPasswordDialogState: EnterPasswordDialogState = HIDDEN
)

enum class EnterPasswordDialogState {
  SHOWN, HIDDEN, HIDDEN_KEEPING_KEYBOARD
}

enum class SettingsBiometricsError {
  LOCKOUT, LOCKOUT_PERMANENT, OTHER
}