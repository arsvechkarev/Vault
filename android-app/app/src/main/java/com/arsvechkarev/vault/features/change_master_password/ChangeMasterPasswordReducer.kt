package com.arsvechkarev.vault.features.change_master_password

import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordCommand.ChangeMasterPassword
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordCommand.CheckPassword
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
import com.arsvechkarev.vault.features.common.Router

class ChangeMasterPasswordReducer(
  private val router: Router
) : DslReducer<ChangeMasterPasswordState, ChangeMasterPasswordEvent,
    ChangeMasterPasswordCommand, Nothing>() {
  
  override fun dslReduce(event: ChangeMasterPasswordEvent) {
    when (event) {
      is OnInitialPasswordChanged -> {
        state { copy(initialPassword = event.text, error = null) }
      }
      is OnRepeatedPasswordChanged -> {
        state { copy(repeatedPassword = event.text, error = null) }
      }
      OnChangeMasterPasswordClicked -> {
        if (state.initialPassword.isEmpty() && state.repeatedPassword.isEmpty()) {
          return
        }
        if (state.initialPassword != state.repeatedPassword) {
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
        router.goBack()
      }
      OnBackPressed -> {
        when (state.dialogType) {
          CONFIRMATION -> state { copy(dialogType = null) }
          NOTIFICATION_AFTER -> router.goBack()
          else -> router.goBack()
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
