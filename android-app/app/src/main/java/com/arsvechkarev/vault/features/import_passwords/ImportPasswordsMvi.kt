package com.arsvechkarev.vault.features.import_passwords

import android.net.Uri

sealed interface ImportPasswordsEvent {
  object PasswordsImportSuccess : ImportPasswordsEvent
  object PasswordsImportFailure : ImportPasswordsEvent
}

sealed interface ImportPasswordsUiEvent : ImportPasswordsEvent {
  class OnSelectedFile(val uri: Uri) : ImportPasswordsUiEvent
  object OnImportPasswordsClicked : ImportPasswordsUiEvent
  object OnConfirmedImportClicked : ImportPasswordsUiEvent
  class OnPasswordEntered(val password: String) : ImportPasswordsUiEvent
  object OnHideErrorDialog : ImportPasswordsUiEvent
  object OnHideInfoDialog : ImportPasswordsUiEvent
  object OnHideSuccessDialog : ImportPasswordsUiEvent
  object OnHidePasswordEnteringDialog : ImportPasswordsUiEvent
  object OnBackPressed : ImportPasswordsUiEvent
}

sealed interface ImportPasswordsCommand {
  class TryImportPasswords(val uri: Uri, val password: String) : ImportPasswordsCommand
  object GoBack : ImportPasswordsCommand
}

data class ImportPasswordsState(
  val selectedFileUri: Uri? = null,
  val enteredPassword: String = "",
  val showLoading: Boolean = false,
  val showEnteringPassword: Boolean = false,
  val infoDialog: ImportPasswordsInfoDialog? = null,
  val showSelectFileError: Boolean = false,
)

enum class ImportPasswordsInfoDialog {
  CONFIRMATION, SUCCESS, FAILURE
}
