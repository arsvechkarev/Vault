package com.arsvechkarev.vault.features.export_passwords

import com.arsvechkarev.vault.core.Router
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsCommand.GetPasswordsFileUri
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsEvent.ExportSuccessful
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsEvent.PasswordsFileUriReceived
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsNews.TryExportPasswords
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnExportPasswordClicked
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnFilenameTextChanged
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnHideMasterPasswordCheckDialog
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnHideViewExportedFileDialog
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnMasterPasswordCheckPassed
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnSelectedFolder
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnViewExportedFileClicked

class ExportPasswordsReducer(
  private val router: Router
) : DslReducer<ExportPasswordsState, ExportPasswordsEvent,
    ExportPasswordsCommand, ExportPasswordsNews>() {
  
  override fun dslReduce(event: ExportPasswordsEvent) {
    when (event) {
      is OnSelectedFolder -> {
        state { copy(folderPath = event.path) }
      }
      is OnFilenameTextChanged -> {
        state { copy(filename = event.text) }
      }
      OnExportPasswordClicked -> {
        state { copy(dialogType = ExportPasswordsDialogType.CHECKING_MASTER_PASSWORD) }
      }
      OnMasterPasswordCheckPassed -> {
        state { copy(dialogType = ExportPasswordsDialogType.LOADING) }
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
      is PasswordsFileUriReceived -> {
        state { copy(dialogType = ExportPasswordsDialogType.LOADING) }
        news(TryExportPasswords(state.folderPath, state.filename, event.uri))
      }
      is ExportSuccessful -> {
        state { copy(dialogType = ExportPasswordsDialogType.SUCCESS_EXPORT) }
      }
      
    }
  }
}
