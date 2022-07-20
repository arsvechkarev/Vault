package com.arsvechkarev.vault.features.creating_master_password

import buisnesslogic.PasswordError
import buisnesslogic.PasswordStrength

typealias CMPState = CreatingMasterPasswordState
typealias CMPEvents = CreatingMasterPasswordEvent
typealias CMPUiEvent = CreatingMasterPasswordUiEvent
typealias CMPCommands = CreatingMasterPasswordCommands
typealias CMPNews = CreatingMasterPasswordNews

sealed interface CreatingMasterPasswordEvent {
  class UpdatePasswordError(val passwordError: PasswordError?) : CMPEvents
  class UpdatePasswordStrength(val passwordStrength: PasswordStrength?) : CMPEvents
  object FinishedAuth : CMPEvents
}

sealed interface CreatingMasterPasswordUiEvent : CreatingMasterPasswordEvent {
  class OnInitialPasswordTyping(val password: String) : CMPUiEvent
  class OnRepeatPasswordTyping(val password: String) : CreatingMasterPasswordUiEvent
  object RequestShowPasswordStrengthDialog : CreatingMasterPasswordUiEvent
  object RequestHidePasswordStrengthDialog : CreatingMasterPasswordUiEvent
  object OnContinueClicked : CreatingMasterPasswordUiEvent
  object OnBackPressed : CreatingMasterPasswordUiEvent
  object OnBackButtonClicked : CreatingMasterPasswordUiEvent
}

sealed interface CreatingMasterPasswordNews {
  object FinishingAuthorization : CreatingMasterPasswordNews
}

sealed interface CreatingMasterPasswordCommands {
  
  sealed interface PasswordCommand : CreatingMasterPasswordCommands {
    class CheckPasswordStrength(val password: String) : PasswordCommand
    class CheckPasswordForErrors(val password: String) : PasswordCommand
  }
  
  class FinishAuth(val password: String) : CreatingMasterPasswordCommands
}

data class CreatingMasterPasswordState(
  val passwordEnteringState: PasswordEnteringState = PasswordEnteringState.INITIAL,
  val passwordStatus: UiPasswordStatus? = null,
  val passwordStrength: PasswordStrength? = null,
  val showPasswordStrengthDialog: Boolean = false,
  val initialPassword: String = "",
  val repeatedPassword: String = "",
  val showErrorText: Boolean = false,
)

enum class UiPasswordStatus {
  OK, TOO_WEAK, TOO_SHORT, EMPTY, PASSWORDS_DONT_MATCH
}

enum class PasswordEnteringState { INITIAL, REPEATING }
