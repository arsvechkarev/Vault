package com.arsvechkarev.vault.features.import_passwords

import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsCommand.TryImportPasswords
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsEvent.PasswordsImportFailure
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsEvent.PasswordsImportSuccess
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsInfoDialog.CONFIRMATION
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsInfoDialog.FAILURE
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsInfoDialog.SUCCESS
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnConfirmedImportClicked
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnHideInfoDialog
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnHidePasswordEnteringDialog
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnImportPasswordsClicked
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnPasswordEntered
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnSelectedFile

class ImportPasswordsReducer(
  private val router: Router
) : DslReducer<ImportPasswordsState, ImportPasswordsEvent, ImportPasswordsCommand, Nothing>() {
  
  override fun dslReduce(event: ImportPasswordsEvent) {
    when (event) {
      is OnSelectedFile -> {
        state { copy(selectedFileUri = event.uri, showSelectFileError = false) }
      }
      OnImportPasswordsClicked -> {
        if (state.selectedFileUri == null) {
          state { copy(showSelectFileError = true) }
        } else {
          state { copy(infoDialog = CONFIRMATION) }
        }
      }
      OnConfirmedImportClicked -> {
        state { copy(infoDialog = null, showEnteringPassword = true) }
      }
      is OnPasswordEntered -> {
        state { copy(enteredPassword = event.password, showLoading = true) }
        commands(TryImportPasswords(checkNotNull(state.selectedFileUri), event.password))
      }
      OnHideInfoDialog -> {
        state { copy(infoDialog = null) }
      }
      OnHidePasswordEnteringDialog -> {
        state { copy(showEnteringPassword = false) }
      }
      OnBackPressed -> {
        when {
          state.showLoading -> return
          state.infoDialog != null -> state { copy(infoDialog = null) }
          state.showEnteringPassword -> state { copy(showEnteringPassword = false) }
          else -> router.goBack()
        }
      }
      PasswordsImportFailure -> {
        state { copy(infoDialog = FAILURE, showLoading = false) }
      }
      PasswordsImportSuccess -> {
        state { copy(infoDialog = SUCCESS, showEnteringPassword = false, showLoading = false) }
      }
    }
  }
}
