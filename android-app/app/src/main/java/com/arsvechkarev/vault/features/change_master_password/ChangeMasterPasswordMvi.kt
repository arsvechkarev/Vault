package com.arsvechkarev.vault.features.change_master_password

import buisnesslogic.Password

sealed interface ChangeMasterPasswordEvent {
  object NewMasterPasswordSaved : ChangeMasterPasswordEvent
  
  sealed interface ValidationResult : ChangeMasterPasswordEvent {
    class Success(val password: Password) : ValidationResult
    object MasterPasswordIsSameAsCurrent : ValidationResult
  }
}

sealed interface ChangeMasterPasswordUiEvent : ChangeMasterPasswordEvent {
  class OnInitialPasswordChanged(val password: Password) : ChangeMasterPasswordUiEvent
  class OnRepeatedPasswordChanged(val password: Password) : ChangeMasterPasswordUiEvent
  object OnConfirmChangePassword : ChangeMasterPasswordUiEvent
  object OnCancelChangePassword : ChangeMasterPasswordUiEvent
  object OnNotificationOkClicked : ChangeMasterPasswordUiEvent
  object OnBackPressed : ChangeMasterPasswordUiEvent
  object OnChangeMasterPasswordClicked : ChangeMasterPasswordUiEvent
}

sealed interface ChangeMasterPasswordCommand {
  class CheckPassword(val password: Password) : ChangeMasterPasswordCommand
  class ChangeMasterPassword(val password: Password) : ChangeMasterPasswordCommand
  object GoBack : ChangeMasterPasswordCommand
}

data class ChangeMasterPasswordState(
  val initialPassword: Password = Password.empty(),
  val repeatedPassword: Password = Password.empty(),
  val dialogType: ChangeMasterPasswordDialogType? = null,
  val error: ChangeMasterPasswordError? = null
)

enum class ChangeMasterPasswordDialogType {
  CONFIRMATION, LOADING, NOTIFICATION_AFTER
}

enum class ChangeMasterPasswordError {
  PASSWORDS_DO_NOT_MATCH, PASSWORD_SAME_AS_CURRENT
}
