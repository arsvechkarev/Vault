package com.arsvechkarev.vault.features.creating_master_password

import buisnesslogic.Password
import buisnesslogic.PasswordStatus
import buisnesslogic.PasswordStrength

typealias CMPState = CreatingMasterPasswordState
typealias CMPEvents = CreatingMasterPasswordEvent
typealias CMPUiEvent = CreatingMasterPasswordUiEvent
typealias CMPCommands = CreatingMasterPasswordCommand
typealias CMPNews = CreatingMasterPasswordNews

sealed interface CreatingMasterPasswordEvent {
  class UpdatedPasswordStatus(val passwordStatus: PasswordStatus) : CMPEvents
  class UpdatedPasswordStrength(val passwordStrength: PasswordStrength?) : CMPEvents
  object FinishedAuth : CMPEvents
}

sealed interface CreatingMasterPasswordUiEvent : CreatingMasterPasswordEvent {
  class OnInitialPasswordTyping(val password: Password) : CMPUiEvent
  class OnRepeatPasswordTyping(val password: Password) : CreatingMasterPasswordUiEvent
  object OnShowPasswordStrengthDialog : CreatingMasterPasswordUiEvent
  object OnHidePasswordStrengthDialog : CreatingMasterPasswordUiEvent
  object OnProceedWithWeakPassword : CreatingMasterPasswordUiEvent
  object OnContinueClicked : CreatingMasterPasswordUiEvent
  object OnBackPressed : CreatingMasterPasswordUiEvent
}

sealed interface CreatingMasterPasswordNews {
  object FinishingAuthorization : CreatingMasterPasswordNews
}

sealed interface CreatingMasterPasswordCommand {
  
  sealed interface PasswordCommand : CreatingMasterPasswordCommand {
    class CheckPasswordStrength(val password: Password) : PasswordCommand
    class CheckPasswordStatus(val password: Password) : PasswordCommand
  }
  
  class FinishAuth(val password: Password) : CreatingMasterPasswordCommand
  
  sealed interface RouterCommand : CreatingMasterPasswordCommand {
    object GoBack : RouterCommand
    object GoToMainListScreen : RouterCommand
  }
}

data class CreatingMasterPasswordState(
  val passwordEnteringState: PasswordEnteringState = PasswordEnteringState.INITIAL,
  val passwordStatus: UiPasswordStatus = UiPasswordStatus.OK,
  val passwordStrength: PasswordStrength? = null,
  val showPasswordTooWeakDialog: Boolean = false,
  val initialPassword: Password = Password.empty(),
  val repeatedPassword: Password = Password.empty(),
)

enum class UiPasswordStatus {
  OK, EMPTY, TOO_WEAK, PASSWORDS_DONT_MATCH
}

enum class PasswordEnteringState { INITIAL, REPEATING }
