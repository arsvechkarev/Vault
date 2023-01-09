package com.arsvechkarev.vault.features.creating_password_entry

import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.NotifyPasswordEntryCreated
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.RouterCommand.GoToCreatePasswordScreen
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.RouterCommand.GoToInfoScreen
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.SavePassword
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.ValidateInput
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.PasswordEntered
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.PasswordEntryCreated
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.SendValidationResult
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.ValidationResult.Fail
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.ValidationResult.Success
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryUiEvent.OnContinueClicked
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryUiEvent.OnLoginTextChanged
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryUiEvent.OnWebsiteNameTextChanged

class CreatingPasswordEntryReducer :
  DslReducer<CreatingPasswordEntryState, CreatingPasswordEntryEvent,
      CreatingPasswordEntryCommand, Nothing>() {
  
  override fun dslReduce(event: CreatingPasswordEntryEvent) {
    when (event) {
      is OnWebsiteNameTextChanged -> {
        state { copy(websiteName = event.text, websiteNameEmpty = false) }
      }
      is OnLoginTextChanged -> {
        state { copy(login = event.text, loginEmpty = false) }
      }
      is OnContinueClicked -> {
        commands(ValidateInput(event.websiteName, event.login))
      }
      OnBackPressed -> {
        commands(GoBack)
      }
      is SendValidationResult -> {
        when (val result = event.validationResult) {
          is Fail -> {
            state {
              copy(
                websiteNameEmpty = result.websiteNameEmpty,
                loginEmpty = result.loginEmpty
              )
            }
          }
          Success -> {
            commands(GoToCreatePasswordScreen)
          }
        }
      }
      is PasswordEntered -> {
        commands(SavePassword(state.websiteName, state.login, event.password))
      }
      is PasswordEntryCreated -> {
        commands(NotifyPasswordEntryCreated, GoToInfoScreen(event.passwordItem))
      }
    }
  }
}