package com.arsvechkarev.vault.features.change_master_password

import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordCommand.ChangeMasterPassword
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordCommand.CheckPassword
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordCommand.GoBack
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordDialogType.CONFIRMATION
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordDialogType.NOTIFICATION_AFTER
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordError.PASSWORD_SAME_AS_CURRENT
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordEvent.NewMasterPasswordSaved
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordEvent.ValidationResult.MasterPasswordIsSameAsCurrent
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordEvent.ValidationResult.Success
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordUiEvent.OnCancelChangePassword
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordUiEvent.OnChangeMasterPasswordClicked
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordUiEvent.OnConfirmChangePassword
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordUiEvent.OnInitialPasswordChanged
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordUiEvent.OnNotificationOkClicked
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordUiEvent.OnRepeatedPasswordChanged

// TODO (9/21/23): Optimize for lags and delays
class ChangeMasterPasswordReducer : DslReducer<ChangeMasterPasswordState, ChangeMasterPasswordEvent,
    ChangeMasterPasswordCommand, Nothing>() {
  
  override fun dslReduce(event: ChangeMasterPasswordEvent) {
    when (event) {
      is OnInitialPasswordChanged -> {
        state { copy(initialPassword = event.password, error = null) }
      }
      
      is OnRepeatedPasswordChanged -> {
        state { copy(repeatedPassword = event.password, error = null) }
      }
      
      OnChangeMasterPasswordClicked -> {
        if (state.initialPassword.stringData.isBlank() && state.repeatedPassword.stringData.isBlank()) {
          return
        }
        if (state.initialPassword.stringData != state.repeatedPassword.stringData) {
          state { copy(error = ChangeMasterPasswordError.PASSWORDS_DO_NOT_MATCH) }
        } else {
          commands(CheckPassword(state.repeatedPassword))
        }
      }
      
      OnConfirmChangePassword -> {
        state { copy(dialogType = ChangeMasterPasswordDialogType.LOADING) }
        commands(ChangeMasterPassword(state.repeatedPassword))
      }
      
      OnCancelChangePassword -> {
        state { copy(dialogType = null) }
      }
      
      OnNotificationOkClicked -> {
        commands(GoBack)
      }
      
      OnBackPressed -> {
        when (state.dialogType) {
          CONFIRMATION -> state { copy(dialogType = null) }
          NOTIFICATION_AFTER -> commands(GoBack)
          else -> commands(GoBack)
        }
      }
      
      MasterPasswordIsSameAsCurrent -> {
        state { copy(dialogType = null, error = PASSWORD_SAME_AS_CURRENT) }
      }
      
      is Success -> {
        state { copy(dialogType = CONFIRMATION) }
      }
      
      NewMasterPasswordSaved -> {
        state { copy(dialogType = NOTIFICATION_AFTER) }
      }
    }
  }
}
