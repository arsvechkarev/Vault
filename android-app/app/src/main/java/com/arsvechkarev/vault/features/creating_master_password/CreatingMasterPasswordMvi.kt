package com.arsvechkarev.vault.features.creating_master_password

import buisnesslogic.PasswordStatus
import buisnesslogic.PasswordStrength

typealias CMPState = CreatingMasterPasswordState
typealias CMPEvents = CreatingMasterPasswordEvent
typealias CMPUiEvent = CreatingMasterPasswordUiEvent
typealias CMPCommands = CreatingMasterPasswordCommands
typealias CMPNews = CreatingMasterPasswordNews

sealed interface CreatingMasterPasswordEvent {
  class PasswordEnteringStateChanged(val state: PasswordEnteringState) : CMPEvents
  class UpdatePasswordStatus(val passwordStatus: PasswordStatus?) : CMPEvents
  class UpdatePasswordStrength(val passwordStrength: PasswordStrength?) : CMPEvents
  object ShowPasswordsMatch : CMPEvents
  object ShowPasswordsDontMatch : CMPEvents
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
  object HideErrorText : CreatingMasterPasswordNews
  object FinishingAuthorization : CreatingMasterPasswordNews
}

sealed interface CreatingMasterPasswordCommands {
  
  sealed interface PasswordCommand : CreatingMasterPasswordCommands {
    class CheckPasswordStrength(val password: String) : PasswordCommand
    class Validate(val password: String) : PasswordCommand
  }
  
  class FinishAuth(val password: String) : CreatingMasterPasswordCommands
}

data class CreatingMasterPasswordState(
  val passwordEnteringState: PasswordEnteringState = PasswordEnteringState.INITIAL,
  val passwordStatus: PasswordStatus? = null,
  val passwordStrength: PasswordStrength? = null,
  val showPasswordStrengthDialog: Boolean = false,
  val initialPassword: String = "",
  val repeatedPassword: String = "",
  val passwordsMatch: Boolean? = null
)

enum class PasswordEnteringState { INITIAL, REPEATING }
