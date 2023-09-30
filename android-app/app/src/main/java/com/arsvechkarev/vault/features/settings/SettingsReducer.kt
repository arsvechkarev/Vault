package com.arsvechkarev.vault.features.settings

import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.HIDDEN
import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.HIDDEN_KEEPING_KEYBOARD
import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.SHOWN
import com.arsvechkarev.vault.features.settings.SettingsCommand.ChangeShowUsernames
import com.arsvechkarev.vault.features.settings.SettingsCommand.ClearImagesCache
import com.arsvechkarev.vault.features.settings.SettingsCommand.GetShowUsernames
import com.arsvechkarev.vault.features.settings.SettingsCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.settings.SettingsCommand.RouterCommand.GoToMasterPasswordScreen
import com.arsvechkarev.vault.features.settings.SettingsEvent.ImagesCacheCleared
import com.arsvechkarev.vault.features.settings.SettingsEvent.MasterPasswordChanged
import com.arsvechkarev.vault.features.settings.SettingsEvent.ShowUsernamesReceived
import com.arsvechkarev.vault.features.settings.SettingsNews.SetShowUsernames
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowImagesCacheCleared
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowMasterPasswordChanged
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.FetchShowUsernamesChecked
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnChangeMasterPasswordClicked
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnClearImagesCacheClicked
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnEnteredPasswordToChangeMasterPassword
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnHideEnterPasswordDialog
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnShowUsernamesChanged

class SettingsReducer : DslReducer<SettingsState, SettingsEvent,
    SettingsCommand, SettingsNews>() {
  
  override fun dslReduce(event: SettingsEvent) {
    when (event) {
      FetchShowUsernamesChecked -> {
        commands(GetShowUsernames)
      }
      is ShowUsernamesReceived -> {
        news(SetShowUsernames(event.showUsernames))
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
      is OnShowUsernamesChanged -> {
        state { copy(showUsernames = event.showUsernames) }
        commands(ChangeShowUsernames(event.showUsernames))
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
      MasterPasswordChanged -> news(ShowMasterPasswordChanged)
      ImagesCacheCleared -> news(ShowImagesCacheCleared)
    }
  }
}
