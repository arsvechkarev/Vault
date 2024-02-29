package com.arsvechkarev.vault.features.import_passwords

import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsCommand.RouterCommand.GoToMainListScreen
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsCommand.TryImportPasswords
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsEvent.PasswordsImportFailure
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsEvent.PasswordsImportSuccess
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsInfoDialog.CONFIRMATION
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsInfoDialog.FAILURE
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnClearKeyFileClicked
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnConfirmedImportClicked
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnHideErrorDialog
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnHideInfoDialog
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnHidePasswordEnteringDialog
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnImportPasswordsClicked
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnPasswordEntered
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnSelectedKeyFile
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnSelectedPasswordsFile

class ImportPasswordsReducer : DslReducer<ImportPasswordsState, ImportPasswordsEvent,
    ImportPasswordsCommand, Nothing>() {
  
  override fun dslReduce(event: ImportPasswordsEvent) {
    when (event) {
      is OnSelectedPasswordsFile -> {
        state { copy(passwordsFileUri = event.uri) }
      }
      is OnSelectedKeyFile -> {
        state { copy(keyFileUri = event.uri) }
      }
      OnImportPasswordsClicked -> {
        when {
          state.askForConfirmation -> state { copy(infoDialog = CONFIRMATION) }
          else -> state { copy(showEnterPasswordDialog = true) }
        }
      }
      OnClearKeyFileClicked -> {
        state { copy(keyFileUri = null) }
      }
      OnConfirmedImportClicked -> {
        state { copy(infoDialog = null, showEnterPasswordDialog = true) }
      }
      is OnPasswordEntered -> {
        state { copy(enteredPassword = event.password, showLoading = true) }
        commands(
          TryImportPasswords(
            checkNotNull(state.passwordsFileUri),
            state.keyFileUri,
            event.password
          )
        )
      }
      OnHideInfoDialog, OnHideErrorDialog -> {
        state { copy(infoDialog = null) }
      }
      OnHidePasswordEnteringDialog -> {
        state { copy(showEnterPasswordDialog = false) }
      }
      OnBackPressed -> {
        when {
          state.showLoading -> return
          state.infoDialog != null -> state { copy(infoDialog = null) }
          state.showEnterPasswordDialog -> state { copy(showEnterPasswordDialog = false) }
          else -> commands(GoBack)
        }
      }
      PasswordsImportFailure -> {
        state { copy(infoDialog = FAILURE, showLoading = false) }
      }
      PasswordsImportSuccess -> {
        commands(GoToMainListScreen)
      }
    }
  }
}
