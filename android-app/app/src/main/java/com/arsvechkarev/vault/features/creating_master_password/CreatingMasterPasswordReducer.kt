package com.arsvechkarev.vault.features.creating_master_password

import buisnesslogic.PasswordError
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommand.FinishAuth
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommand.PasswordCommand.CheckPasswordForErrors
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommand.PasswordCommand.CheckPasswordStrength
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.FinishedAuth
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.UpdatePasswordError
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.UpdatePasswordStrength
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordNews.FinishingAuthorization
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.OnBackButtonClicked
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.OnContinueClicked
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.OnInitialPasswordTyping
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.OnRepeatPasswordTyping
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.RequestHidePasswordStrengthDialog
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.RequestShowPasswordStrengthDialog
import com.arsvechkarev.vault.features.creating_master_password.PasswordEnteringState.INITIAL
import com.arsvechkarev.vault.features.creating_master_password.PasswordEnteringState.REPEATING
import com.arsvechkarev.vault.features.creating_master_password.UiPasswordStatus.PASSWORDS_DONT_MATCH

class CreatingMasterPasswordReducer(
  private val router: Router
) : DslReducer<CMPState, CMPEvents, CMPCommands, CMPNews>() {
  
  override fun dslReduce(event: CreatingMasterPasswordEvent) {
    when (event) {
      is CMPUiEvent -> {
        handleUIEvent(event)
      }
      is UpdatePasswordError -> {
        val showErrorText = event.passwordError != null
        val passwordEnteringState = if (event.passwordError != null) INITIAL else REPEATING
        val passwordStatus = when (event.passwordError) {
          PasswordError.EMPTY -> UiPasswordStatus.EMPTY
          PasswordError.TOO_SHORT -> UiPasswordStatus.TOO_SHORT
          PasswordError.TOO_WEAK -> UiPasswordStatus.TOO_WEAK
          null -> UiPasswordStatus.OK
        }
        state {
          copy(
            passwordStatus = passwordStatus,
            passwordEnteringState = passwordEnteringState,
            showErrorText = showErrorText
          )
        }
      }
      is UpdatePasswordStrength -> {
        state { copy(passwordStrength = event.passwordStrength) }
      }
      FinishedAuth -> {
        router.switchToNewRoot(Screens.MainListScreen)
      }
    }
  }
  
  private fun handleUIEvent(event: CMPUiEvent) {
    when (event) {
      is OnInitialPasswordTyping -> {
        state { copy(initialPassword = event.password, showErrorText = false) }
        commands(CheckPasswordStrength(event.password))
      }
      is OnRepeatPasswordTyping -> {
        state { copy(repeatedPassword = event.password, showErrorText = false) }
      }
      OnBackPressed, OnBackButtonClicked -> {
        if (state.showPasswordStrengthDialog) {
          state { copy(showPasswordStrengthDialog = false) }
        } else {
          when (state.passwordEnteringState) {
            INITIAL -> {
              router.goBack()
            }
            REPEATING -> {
              state { copy(passwordEnteringState = INITIAL) }
            }
          }
        }
      }
      OnContinueClicked -> {
        when (state.passwordEnteringState) {
          INITIAL -> {
            commands(CheckPasswordForErrors(state.initialPassword))
          }
          REPEATING -> {
            if (state.repeatedPassword == state.initialPassword) {
              state { copy(showErrorText = false) }
              commands(FinishAuth(state.initialPassword))
              news(FinishingAuthorization)
            } else {
              state { copy(passwordStatus = PASSWORDS_DONT_MATCH, showErrorText = true) }
            }
          }
        }
      }
      RequestShowPasswordStrengthDialog -> {
        state { copy(showPasswordStrengthDialog = true) }
      }
      RequestHidePasswordStrengthDialog -> {
        state { copy(showPasswordStrengthDialog = false) }
      }
    }
  }
}
