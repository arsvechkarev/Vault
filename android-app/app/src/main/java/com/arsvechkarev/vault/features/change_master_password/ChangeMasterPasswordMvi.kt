package com.arsvechkarev.vault.features.change_master_password

sealed interface ChangeMasterPasswordEvent {
  object NewMasterPasswordSaved : ChangeMasterPasswordEvent
  
  sealed interface ValidationResult : ChangeMasterPasswordEvent {
    class Success(val password: String) : ValidationResult
    object MasterPasswordIsSameAsCurrent : ValidationResult
  }
}

sealed interface ChangeMasterPasswordUiEvent : ChangeMasterPasswordEvent {
  class OnInitialPasswordChanged(val text: String) : ChangeMasterPasswordUiEvent
  class OnRepeatedPasswordChanged(val text: String) : ChangeMasterPasswordUiEvent
  object OnConfirmChangePassword : ChangeMasterPasswordUiEvent
  object OnCancelChangePassword : ChangeMasterPasswordUiEvent
  object OnNotificationOkClicked : ChangeMasterPasswordUiEvent
  object OnBackPressed : ChangeMasterPasswordUiEvent
  object OnChangeMasterPasswordClicked : ChangeMasterPasswordUiEvent
}

sealed interface ChangeMasterPasswordCommand {
  
  class CheckPassword(val password: String) : ChangeMasterPasswordCommand
  class ChangeMasterPassword(val password: String) : ChangeMasterPasswordCommand
}

data class ChangeMasterPasswordState(
  val initialPassword: String = "",
  val repeatedPassword: String = "",
  val dialogType: ChangeMasterPasswordDialogType? = null,
  val error: ChangeMasterPasswordError? = null
)

enum class ChangeMasterPasswordDialogType {
  CONFIRMATION, LOADING, NOTIFICATION_AFTER
}

enum class ChangeMasterPasswordError {
  PASSWORDS_DO_NOT_MATCH, PASSWORD_SAME_AS_CURRENT
}
