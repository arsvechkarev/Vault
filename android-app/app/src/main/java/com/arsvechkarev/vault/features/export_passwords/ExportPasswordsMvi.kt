package com.arsvechkarev.vault.features.export_passwords

import android.net.Uri

sealed interface ExportPasswordsEvent {
  object ExportedPasswords : ExportPasswordsEvent
  class PasswordsFileUriReceived(val uri: String) : ExportPasswordsEvent
  class CalculatedFilenameFromUri(val filename: String) : ExportPasswordsEvent
}

sealed interface ExportPasswordsUiEvent : ExportPasswordsEvent {
  class OnSelectedFolder(val path: String) : ExportPasswordsUiEvent
  class OnFilenameTextChanged(val text: String) : ExportPasswordsUiEvent
  class OnViewExportedFileClicked(val path: String) : ExportPasswordsUiEvent
  object OnExportPasswordClicked : ExportPasswordsUiEvent
  object OnHideViewExportedFileDialog : ExportPasswordsUiEvent
  class OnFileForPasswordsExportCreated(val uri: Uri) : ExportPasswordsUiEvent
  object OnBackPressed : ExportPasswordsUiEvent
}

sealed interface ExportPasswordsCommand {
  class ExportPasswords(val uri: Uri) : ExportPasswordsCommand
  object GetPasswordsFileUri : ExportPasswordsCommand
  class CalculateFilenameFromUri(val uri: Uri, val fallback: String) : ExportPasswordsCommand
  object GoBack : ExportPasswordsCommand
}

sealed interface ExportPasswordsNews {
  
  class TryExportPasswords(val filename: String) : ExportPasswordsNews
}

data class ExportPasswordsState(
  val folderPath: String = "",
  val filename: String = "",
  val exportedFileUri: Uri? = null,
  val showSuccessDialog: Boolean = false,
  val showSelectFolderError: Boolean = false,
  val showEnterFilenameError: Boolean = false,
)
