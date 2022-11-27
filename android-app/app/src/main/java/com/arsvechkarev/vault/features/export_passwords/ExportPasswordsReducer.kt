package com.arsvechkarev.vault.features.export_passwords

import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsCommand.CalculateFilenameFromUri
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsCommand.GetPasswordsFileUri
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsEvent.CalculatedFilenameFromUri
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsEvent.PasswordsFileUriReceived
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsNews.TryExportPasswords
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnExportPasswordClicked
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnFilenameTextChanged
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnHideMasterPasswordCheckDialog
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnHideViewExportedFileDialog
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnMasterPasswordCheckPassed
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnPasswordsExported
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnSelectedFolder
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnViewExportedFileClicked

class ExportPasswordsReducer(
  private val router: Router
) : DslReducer<ExportPasswordsState, ExportPasswordsEvent,
    ExportPasswordsCommand, ExportPasswordsNews>() {
  
  override fun dslReduce(event: ExportPasswordsEvent) {
    when (event) {
      is OnSelectedFolder -> {
        state { copy(folderPath = event.path, showSelectFolderError = false) }
      }
      is OnFilenameTextChanged -> {
        state { copy(filename = event.text, showEnterFilenameError = false) }
      }
      OnExportPasswordClicked -> {
        if (state.folderPath.isNotBlank() && state.filename.isNotBlank()) {
          state { copy(dialogType = ExportPasswordsDialogType.CHECKING_MASTER_PASSWORD) }
        } else {
          state {
            copy(
              showSelectFolderError = state.folderPath.isBlank(),
              showEnterFilenameError = state.filename.isBlank(),
            )
          }
        }
      }
      OnMasterPasswordCheckPassed -> {
        state { copy(dialogType = null) }
        commands(GetPasswordsFileUri)
      }
      is OnViewExportedFileClicked -> {
        commands()
      }
      OnHideMasterPasswordCheckDialog, OnHideViewExportedFileDialog -> {
        state { copy(dialogType = null) }
      }
      OnBackPressed -> {
        if (state.dialogType == null) {
          router.goBack()
        } else {
          state { copy(dialogType = null) }
        }
      }
      is CalculatedFilenameFromUri -> {
        state { copy(filename = event.filename) }
      }
      is PasswordsFileUriReceived -> {
        news(TryExportPasswords(state.folderPath, state.filename, event.uri))
      }
      is OnPasswordsExported -> {
        state {
          copy(
            dialogType = ExportPasswordsDialogType.SUCCESS_EXPORT,
            exportedFileUri = event.uri
          )
        }
        commands(CalculateFilenameFromUri(event.uri, fallback = state.filename))
      }
    }
  }
}
