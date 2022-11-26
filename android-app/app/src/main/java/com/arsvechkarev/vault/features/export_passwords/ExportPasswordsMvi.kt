package com.arsvechkarev.vault.features.export_passwords

sealed interface ExportPasswordsEvent {
  class PasswordsFileUriReceived(val uri: String) : ExportPasswordsEvent
  class ExportSuccessful(val uri: String) : ExportPasswordsEvent
}

sealed interface ExportPasswordsUiEvent : ExportPasswordsEvent {
  class OnSelectedFolder(val path: String) : ExportPasswordsUiEvent
  class OnFilenameTextChanged(val text: String) : ExportPasswordsUiEvent
  class OnViewExportedFileClicked(val path: String) : ExportPasswordsUiEvent
  object OnExportPasswordClicked : ExportPasswordsUiEvent
  object OnMasterPasswordCheckPassed : ExportPasswordsUiEvent
  object OnHideMasterPasswordCheckDialog : ExportPasswordsUiEvent
  object OnHideViewExportedFileDialog : ExportPasswordsUiEvent
  object OnBackPressed : ExportPasswordsUiEvent
}

sealed interface ExportPasswordsCommand {
  object GetPasswordsFileUri : ExportPasswordsCommand
  class ViewExportedPasswords(val uri: String) : ExportPasswordsCommand
}

sealed interface ExportPasswordsNews {
  
  class TryExportPasswords(
    val folderPath: String,
    val filename: String,
    val fileUri: String
  ) : ExportPasswordsNews
}

data class ExportPasswordsState(
  val folderPath: String = "",
  val filename: String = "",
  val exportedFilePath: String = "",
  val dialogType: ExportPasswordsDialogType? = null
)

enum class ExportPasswordsDialogType {
  CHECKING_MASTER_PASSWORD, LOADING, SUCCESS_EXPORT
}
