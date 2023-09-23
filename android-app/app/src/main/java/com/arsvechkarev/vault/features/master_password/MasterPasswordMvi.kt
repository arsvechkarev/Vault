package com.arsvechkarev.vault.features.master_password

import android.os.Parcelable
import buisnesslogic.Password
import buisnesslogic.PasswordStatus
import buisnesslogic.PasswordStrength
import kotlinx.parcelize.Parcelize

sealed interface MasterPasswordEvent {
  class UpdatedPasswordStatus(val passwordStatus: PasswordStatus) : MasterPasswordEvent
  object PasswordIsTheSameAsCurrent : MasterPasswordEvent
  class UpdatedPasswordStrength(val passwordStrength: PasswordStrength?) : MasterPasswordEvent
  object FinishedMasterPasswordSaving : MasterPasswordEvent
}

sealed interface MasterPasswordUiEvent : MasterPasswordEvent {
  class OnInitialPasswordTyping(val password: Password) : MasterPasswordUiEvent
  class OnRepeatPasswordTyping(val password: Password) : MasterPasswordUiEvent
  object OnShowPasswordStrengthDialog : MasterPasswordUiEvent
  object OnHidePasswordStrengthDialog : MasterPasswordUiEvent
  object OnProceedWithWeakPassword : MasterPasswordUiEvent
  object OnContinueClicked : MasterPasswordUiEvent
  object OnCancelChangePassword : MasterPasswordUiEvent
  object OnConfirmChangePassword : MasterPasswordUiEvent
  object OnBackPressed : MasterPasswordUiEvent
}

sealed interface MasterPasswordNews {
  object FinishingSavingMasterPassword : MasterPasswordNews
}

sealed interface MasterPasswordCommand {
  
  sealed interface PasswordCommand : MasterPasswordCommand {
    class CheckPasswordStrength(val password: Password) : PasswordCommand
    class CheckPasswordStatus(val password: Password) : PasswordCommand
  }
  
  class CreateNewMasterPassword(val password: Password) : MasterPasswordCommand
  class ChangeExistingMasterPassword(val password: Password) : MasterPasswordCommand
  
  sealed interface RouterCommand : MasterPasswordCommand {
    object GoBack : RouterCommand
    object GoToMainListScreen : RouterCommand
  }
}

data class MasterPasswordState(
  val mode: MasterPasswordScreenMode,
  val passwordEnteringState: PasswordEnteringState = PasswordEnteringState.INITIAL,
  val initialPassword: Password = Password.empty(),
  val repeatedPassword: Password = Password.empty(),
  val passwordStatus: UiPasswordStatus = UiPasswordStatus.OK,
  val passwordStrength: PasswordStrength? = null,
  val showPasswordTooWeakDialog: Boolean = false,
  val showPasswordChangeConfirmationDialog: Boolean = false
)

@Parcelize
enum class MasterPasswordScreenMode : Parcelable {
  CREATING_NEW, CHANGE_EXISTING
}

enum class UiPasswordStatus {
  OK, EMPTY, TOO_WEAK, PASSWORDS_DONT_MATCH, PASSWORD_SAME_AS_CURRENT
}

enum class PasswordEnteringState { INITIAL, REPEATING }
