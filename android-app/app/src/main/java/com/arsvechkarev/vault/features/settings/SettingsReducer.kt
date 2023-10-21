package com.arsvechkarev.vault.features.settings

import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEvent
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEvent.ErrorType
import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.HIDDEN
import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.HIDDEN_KEEPING_KEYBOARD
import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.SHOWN
import com.arsvechkarev.vault.features.settings.SettingsBiometricsError.LOCKOUT
import com.arsvechkarev.vault.features.settings.SettingsBiometricsError.LOCKOUT_PERMANENT
import com.arsvechkarev.vault.features.settings.SettingsBiometricsError.OTHER
import com.arsvechkarev.vault.features.settings.SettingsCommand.BackupCommand.DisableStorageBackup
import com.arsvechkarev.vault.features.settings.SettingsCommand.BackupCommand.EnableStorageBackup
import com.arsvechkarev.vault.features.settings.SettingsCommand.BackupCommand.PerformBackup
import com.arsvechkarev.vault.features.settings.SettingsCommand.ChangeShowUsernames
import com.arsvechkarev.vault.features.settings.SettingsCommand.ClearImagesCache
import com.arsvechkarev.vault.features.settings.SettingsCommand.DisableBiometrics
import com.arsvechkarev.vault.features.settings.SettingsCommand.EnableBiometrics
import com.arsvechkarev.vault.features.settings.SettingsCommand.FetchData
import com.arsvechkarev.vault.features.settings.SettingsCommand.FetchStorageBackupInfo
import com.arsvechkarev.vault.features.settings.SettingsCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.settings.SettingsCommand.RouterCommand.GoToMasterPasswordScreen
import com.arsvechkarev.vault.features.settings.SettingsEvent.BiometricsEnabled
import com.arsvechkarev.vault.features.settings.SettingsEvent.ImagesCacheCleared
import com.arsvechkarev.vault.features.settings.SettingsEvent.PerformedBackup
import com.arsvechkarev.vault.features.settings.SettingsEvent.ReceivedBiometricsAvailable
import com.arsvechkarev.vault.features.settings.SettingsEvent.ReceivedBiometricsEnabled
import com.arsvechkarev.vault.features.settings.SettingsEvent.ReceivedShowUsernames
import com.arsvechkarev.vault.features.settings.SettingsEvent.ReceivedStorageBackupEnabled
import com.arsvechkarev.vault.features.settings.SettingsEvent.StorageBackupDisabled
import com.arsvechkarev.vault.features.settings.SettingsEvent.StorageBackupEnabled
import com.arsvechkarev.vault.features.settings.SettingsNews.LaunchFolderSelection
import com.arsvechkarev.vault.features.settings.SettingsNews.SetBiometricsEnabled
import com.arsvechkarev.vault.features.settings.SettingsNews.SetShowUsernames
import com.arsvechkarev.vault.features.settings.SettingsNews.SetStorageBackupEnabled
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowBackupPerformed
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowBiometricsEnabled
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowBiometricsError
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowBiometricsPrompt
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowImagesCacheCleared
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowMasterPasswordChanged
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowStorageBackupEnabled
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnAppearedOnScreen
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnBackupNowClicked
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnBiometricsEvent
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnChangeMasterPasswordClicked
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnClearImagesCacheClicked
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnEnableBiometricsChanged
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnEnableStorageBackupChanged
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnEnteredPasswordToChangeMasterPassword
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnHideEnableBiometricsDialog
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnHideEnterPasswordDialog
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnInit
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnMasterPasswordChangedReceived
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnProceedEnableBiometricsDialog
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnSelectBackupFolderClicked
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnSelectedBackupFolder
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnShowUsernamesChanged

class SettingsReducer : DslReducer<SettingsState, SettingsEvent,
    SettingsCommand, SettingsNews>() {
  
  override fun dslReduce(event: SettingsEvent) {
    when (event) {
      OnInit -> {
        commands(FetchData)
      }
      OnAppearedOnScreen -> {
        commands(FetchStorageBackupInfo)
      }
      is ReceivedShowUsernames -> {
        news(SetShowUsernames(event.showUsernames))
      }
      is ReceivedBiometricsAvailable -> {
        state { copy(biometricsAvailable = event.available) }
      }
      is ReceivedBiometricsEnabled -> {
        state { copy(biometricsEnabled = event.enabled) }
        news(SetBiometricsEnabled(event.enabled, animate = false))
      }
      is ReceivedStorageBackupEnabled -> {
        state {
          copy(
            storageBackupEnabled = event.enabled,
            storageBackupFolderUri = event.backupFolderUri,
            storageBackupLatestDate = event.latestBackupDate
          )
        }
        news(SetStorageBackupEnabled(event.enabled))
      }
      OnChangeMasterPasswordClicked -> {
        state { copy(enterPasswordDialogState = SHOWN) }
      }
      OnEnteredPasswordToChangeMasterPassword -> {
        state { copy(enterPasswordDialogState = HIDDEN_KEEPING_KEYBOARD) }
        commands(GoToMasterPasswordScreen)
      }
      OnHideEnterPasswordDialog -> {
        state { copy(enterPasswordDialogState = HIDDEN) }
      }
      OnMasterPasswordChangedReceived -> {
        if (state.biometricsEnabled) {
          state { copy(showEnableBiometricsDialog = true, biometricsEnabled = false) }
          news(SetBiometricsEnabled(enabled = false, animate = false))
          commands(DisableBiometrics)
        } else {
          news(ShowMasterPasswordChanged)
        }
      }
      is OnShowUsernamesChanged -> {
        commands(ChangeShowUsernames(event.showUsernames))
      }
      is OnEnableBiometricsChanged -> {
        if (event.enabled) {
          news(ShowBiometricsPrompt)
        } else {
          commands(DisableBiometrics)
        }
      }
      is OnBiometricsEvent -> {
        when (event.event) {
          is BiometricsEvent.Success -> {
            commands(EnableBiometrics(event.event.cryptography))
          }
          is BiometricsEvent.Error -> {
            state { copy(biometricsEnabled = false) }
            val news = buildList {
              when (event.event.error) {
                ErrorType.LOCKOUT -> add(ShowBiometricsError(LOCKOUT))
                ErrorType.LOCKOUT_PERMANENT -> add(ShowBiometricsError(LOCKOUT_PERMANENT))
                ErrorType.OTHER -> add(ShowBiometricsError(OTHER))
                else -> Unit
              }
              add(SetBiometricsEnabled(enabled = false, animate = true))
            }
            news(*news.toTypedArray())
          }
        }
      }
      OnProceedEnableBiometricsDialog -> {
        state { copy(showEnableBiometricsDialog = false) }
        news(ShowBiometricsPrompt)
      }
      OnHideEnableBiometricsDialog -> {
        state { copy(showEnableBiometricsDialog = false) }
      }
      is OnEnableStorageBackupChanged -> {
        if (event.enabled) {
          val backupFolderUri = state.storageBackupFolderUri
          if (backupFolderUri != null) {
            commands(EnableStorageBackup(backupFolderUri))
          } else {
            news(LaunchFolderSelection(initialUri = null))
          }
        } else {
          commands(DisableStorageBackup)
        }
      }
      OnSelectBackupFolderClicked -> {
        if (state.storageBackupEnabled) {
          news(LaunchFolderSelection(initialUri = state.storageBackupFolderUri))
        }
      }
      is OnSelectedBackupFolder -> {
        commands(EnableStorageBackup(event.uri))
      }
      OnBackupNowClicked -> {
        if (state.storageBackupEnabled) {
          commands(PerformBackup)
          state { copy(showLoadingBackingUp = true) }
        }
      }
      OnClearImagesCacheClicked -> {
        commands(ClearImagesCache)
      }
      OnBackPressed -> {
        if (state.enterPasswordDialogState == SHOWN) {
          state { copy(enterPasswordDialogState = HIDDEN) }
        } else {
          commands(GoBack)
        }
      }
      BiometricsEnabled -> {
        state { copy(biometricsEnabled = true) }
        news(SetBiometricsEnabled(enabled = true, animate = true), ShowBiometricsEnabled)
      }
      is StorageBackupEnabled -> {
        state { copy(storageBackupEnabled = true, storageBackupFolderUri = event.backupFolderUri) }
        news(ShowStorageBackupEnabled)
      }
      StorageBackupDisabled -> {
        state { copy(storageBackupEnabled = false) }
      }
      PerformedBackup -> {
        state { copy(showLoadingBackingUp = false) }
        commands(FetchStorageBackupInfo)
        news(ShowBackupPerformed)
      }
      ImagesCacheCleared -> {
        news(ShowImagesCacheCleared)
      }
    }
  }
}
