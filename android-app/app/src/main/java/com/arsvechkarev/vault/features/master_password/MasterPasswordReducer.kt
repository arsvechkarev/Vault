package com.arsvechkarev.vault.features.master_password

import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.master_password.MasterPasswordCommand.ChangeExistingMasterPassword
import com.arsvechkarev.vault.features.master_password.MasterPasswordCommand.CreateNewMasterPassword
import com.arsvechkarev.vault.features.master_password.MasterPasswordCommand.PasswordCommand.CheckPasswordStatus
import com.arsvechkarev.vault.features.master_password.MasterPasswordCommand.PasswordCommand.CheckPasswordStrength
import com.arsvechkarev.vault.features.master_password.MasterPasswordCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.master_password.MasterPasswordCommand.RouterCommand.GoToMainListScreen
import com.arsvechkarev.vault.features.master_password.MasterPasswordEvent.FinishedMasterPasswordSaving
import com.arsvechkarev.vault.features.master_password.MasterPasswordEvent.PasswordIsTheSameAsCurrent
import com.arsvechkarev.vault.features.master_password.MasterPasswordEvent.UpdatedPasswordStatus
import com.arsvechkarev.vault.features.master_password.MasterPasswordEvent.UpdatedPasswordStrength
import com.arsvechkarev.vault.features.master_password.MasterPasswordNews.FinishingSavingMasterPassword
import com.arsvechkarev.vault.features.master_password.MasterPasswordScreenMode.CHANGE_EXISTING
import com.arsvechkarev.vault.features.master_password.MasterPasswordScreenMode.CREATING_NEW
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnCancelChangePassword
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnConfirmChangePassword
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnContinueClicked
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnHidePasswordStrengthDialog
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnInitialPasswordTyping
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnProceedWithWeakPassword
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnRepeatPasswordTyping
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnShowPasswordStrengthDialog
import com.arsvechkarev.vault.features.master_password.PasswordEnteringState.INITIAL
import com.arsvechkarev.vault.features.master_password.PasswordEnteringState.REPEATING
import com.arsvechkarev.vault.features.master_password.UiPasswordStatus.PASSWORD_SAME_AS_CURRENT
import domain.PasswordStatus

class MasterPasswordReducer :
  DslReducer<MasterPasswordState, MasterPasswordEvent, MasterPasswordCommand, MasterPasswordNews>() {
  
  override fun dslReduce(event: MasterPasswordEvent) {
    when (event) {
      is OnInitialPasswordTyping -> {
        if (event.password != state.initialPassword) {
          state { copy(initialPassword = event.password, passwordStatus = UiPasswordStatus.OK) }
          commands(CheckPasswordStrength(event.password))
        }
      }
      is OnRepeatPasswordTyping -> {
        if (event.password != state.repeatedPassword) {
          state { copy(repeatedPassword = event.password, passwordStatus = UiPasswordStatus.OK) }
        }
      }
      OnContinueClicked -> {
        when (state.passwordEnteringState) {
          INITIAL -> {
            commands(CheckPasswordStatus(state.initialPassword))
          }
          REPEATING -> {
            if (state.repeatedPassword == state.initialPassword) {
              state { copy(passwordStatus = UiPasswordStatus.OK) }
              when (state.mode) {
                CREATING_NEW -> {
                  commands(CreateNewMasterPassword(state.initialPassword))
                  news(FinishingSavingMasterPassword)
                }
                CHANGE_EXISTING -> state { copy(showPasswordChangeConfirmationDialog = true) }
              }
            } else {
              state { copy(passwordStatus = UiPasswordStatus.PASSWORDS_DONT_MATCH) }
            }
          }
        }
      }
      OnBackPressed -> {
        when {
          state.showPasswordChangeConfirmationDialog -> {
            state { copy(showPasswordChangeConfirmationDialog = false) }
          }
          state.showPasswordTooWeakDialog -> {
            state { copy(showPasswordTooWeakDialog = false) }
          }
          else -> {
            when (state.passwordEnteringState) {
              INITIAL -> commands(GoBack)
              REPEATING -> state { copy(passwordEnteringState = INITIAL) }
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
      PasswordIsTheSameAsCurrent -> {
        state { copy(passwordStatus = PASSWORD_SAME_AS_CURRENT) }
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
      OnCancelChangePassword -> {
        state { copy(showPasswordChangeConfirmationDialog = false) }
      }
      OnConfirmChangePassword -> {
        state { copy(showPasswordChangeConfirmationDialog = false) }
        commands(ChangeExistingMasterPassword(state.initialPassword))
        news(FinishingSavingMasterPassword)
      }
      FinishedMasterPasswordSaving -> {
        when (state.mode) {
          CREATING_NEW -> commands(GoToMainListScreen)
          CHANGE_EXISTING -> commands(GoBack)
        }
      }
    }
  }
}
