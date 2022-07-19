package com.arsvechkarev.vault.features.creating_master_password

import buisnesslogic.PasswordStatus.OK
import com.arsvechkarev.vault.core.Router
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommands.FinishAuth
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommands.PasswordCommand.CheckPasswordStrength
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommands.PasswordCommand.Validate
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.FinishedAuth
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.PasswordEnteringStateChanged
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.ShowPasswordsDontMatch
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.ShowPasswordsMatch
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.UpdatePasswordStatus
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

class CreatingMasterPasswordReducer(
  private val router: Router
) : DslReducer<CMPState, CMPEvents, CMPCommands, CMPNews>() {
  
  override fun dslReduce(event: CreatingMasterPasswordEvent) {
    when (event) {
      is CMPUiEvent -> {
        handleUIEvent(event)
      }
      ShowPasswordsMatch -> {
        state { copy(passwordsMatch = true, showErrorText = false) }
      }
      ShowPasswordsDontMatch -> {
        state { copy(passwordsMatch = false, showErrorText = true) }
      }
      is PasswordEnteringStateChanged -> {
        state { copy(passwordEnteringState = event.state) }
      }
      is UpdatePasswordStatus -> {
        val (passwordEnteringState, showErrorText) = if (event.passwordStatus == OK) {
          Pair(REPEATING, false)
        } else {
          Pair(state.passwordEnteringState, true)
        }
        state {
          copy(
            passwordStatus = event.passwordStatus,
            passwordEnteringState = passwordEnteringState,
            showErrorText = showErrorText
          )
        }
      }
      is UpdatePasswordStrength -> {
        state { copy(passwordStrength = event.passwordStrength) }
      }
      FinishedAuth -> {
        router.switchToNewRoot(Screens.ServicesListScreen)
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
        state { copy(showErrorText = false) }
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
            commands(Validate(state.initialPassword))
          }
          REPEATING -> {
            if (state.initialPassword != "" && state.repeatedPassword == state.initialPassword) {
              state { copy(passwordsMatch = true, showErrorText = false) }
              commands(FinishAuth(state.initialPassword))
              news(FinishingAuthorization)
            } else {
              state { copy(passwordsMatch = false, showErrorText = true) }
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