package com.arsvechkarev.vault.features.import_passwords

import android.net.Uri
import domain.Password

sealed interface ImportPasswordsEvent {
  object PasswordsImportSuccess : ImportPasswordsEvent
  object PasswordsImportFailure : ImportPasswordsEvent
}

sealed interface ImportPasswordsUiEvent : ImportPasswordsEvent {
  class OnSelectedPasswordsFile(val uri: Uri) : ImportPasswordsUiEvent
  class OnSelectedKeyFile(val uri: Uri) : ImportPasswordsUiEvent
  object OnImportPasswordsClicked : ImportPasswordsUiEvent
  object OnClearKeyFileClicked : ImportPasswordsUiEvent
  object OnConfirmedImportClicked : ImportPasswordsUiEvent
  class OnPasswordEntered(val password: Password) : ImportPasswordsUiEvent
  object OnHideErrorDialog : ImportPasswordsUiEvent
  object OnHideInfoDialog : ImportPasswordsUiEvent
  object OnHidePasswordEnteringDialog : ImportPasswordsUiEvent
  object OnBackPressed : ImportPasswordsUiEvent
}

sealed interface ImportPasswordsCommand {
  class TryImportPasswords(
    val passwordsFileUri: Uri,
    val keyFileUri: Uri?,
    val password: Password
  ) : ImportPasswordsCommand
  
  sealed interface RouterCommand : ImportPasswordsCommand {
    object GoBack : RouterCommand
    object GoToMainListScreen : RouterCommand
  }
}

data class ImportPasswordsState(
  val passwordsFileUri: Uri,
  val keyFileUri: Uri? = null,
  val askForConfirmation: Boolean = true,
  val enteredPassword: Password = Password.empty(),
  val showLoading: Boolean = false,
  val showEnterPasswordDialog: Boolean = false,
  val infoDialog: ImportPasswordsInfoDialog? = null
)

enum class ImportPasswordsInfoDialog {
  CONFIRMATION, FAILURE
}
