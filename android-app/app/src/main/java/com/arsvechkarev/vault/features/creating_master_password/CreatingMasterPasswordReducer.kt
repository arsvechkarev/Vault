package com.arsvechkarev.vault.features.creating_master_password

import buisnesslogic.PasswordStatus
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommand.FinishAuth
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommand.PasswordCommand.CheckPasswordStatus
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommand.PasswordCommand.CheckPasswordStrength
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommand.RouterCommand.GoToMainListScreen
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.FinishedAuth
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.UpdatedPasswordStatus
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.UpdatedPasswordStrength
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordNews.FinishingAuthorization
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.OnContinueClicked
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.OnHidePasswordStrengthDialog
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.OnInitialPasswordTyping
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.OnProceedWithWeakPassword
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.OnRepeatPasswordTyping
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.OnShowPasswordStrengthDialog
import com.arsvechkarev.vault.features.creating_master_password.PasswordEnteringState.INITIAL
import com.arsvechkarev.vault.features.creating_master_password.PasswordEnteringState.REPEATING

class CreatingMasterPasswordReducer : DslReducer<CMPState, CMPEvents, CMPCommands, CMPNews>() {
  
  override fun dslReduce(event: CreatingMasterPasswordEvent) {
    when (event) {
      is OnInitialPasswordTyping -> {
        state { copy(initialPassword = event.password, passwordStatus = UiPasswordStatus.OK) }
        commands(CheckPasswordStrength(event.password))
      }
      
      is OnRepeatPasswordTyping -> {
        state { copy(repeatedPassword = event.password, passwordStatus = UiPasswordStatus.OK) }
      }
      
      OnContinueClicked -> {
        when (state.passwordEnteringState) {
          INITIAL -> {
            commands(CheckPasswordStatus(state.initialPassword))
          }
          
          REPEATING -> {
            if (state.repeatedPassword.stringData == state.initialPassword.stringData) {
              state { copy(passwordStatus = UiPasswordStatus.OK) }
              commands(FinishAuth(state.initialPassword))
              news(FinishingAuthorization)
            } else {
              state { copy(passwordStatus = UiPasswordStatus.PASSWORDS_DONT_MATCH) }
            }
          }
        }
      }
      
      OnBackPressed -> {
        if (state.showPasswordTooWeakDialog) {
          state { copy(showPasswordTooWeakDialog = false) }
        } else {
          when (state.passwordEnteringState) {
            INITIAL -> {
              commands(GoBack)
            }
            
            REPEATING -> {
              state { copy(passwordEnteringState = INITIAL) }
            }
          }
        }
      }
      
      OnHidePasswordStrengthDialog -> {
        state { copy(showPasswordTooWeakDialog = false) }
      }
      
      OnShowPasswordStrengthDialog -> {
        state { copy(showPasswordTooWeakDialog = true) }
      }
      
      is UpdatedPasswordStatus -> {
        when (event.passwordStatus) {
          PasswordStatus.OK -> state { copy(passwordEnteringState = REPEATING) }
          PasswordStatus.EMPTY -> state { copy(passwordStatus = UiPasswordStatus.EMPTY) }
          PasswordStatus.TOO_WEAK -> state { copy(passwordStatus = UiPasswordStatus.TOO_WEAK) }
        }
      }
      
      is UpdatedPasswordStrength -> {
        state { copy(passwordStrength = event.passwordStrength) }
      }
      
      OnProceedWithWeakPassword -> {
        state {
          copy(
            passwordEnteringState = REPEATING,
            showPasswordTooWeakDialog = false,
            passwordStatus = UiPasswordStatus.OK
          )
        }
      }
      
      FinishedAuth -> {
        commands(GoToMainListScreen)
      }
    }
  }
  
}
