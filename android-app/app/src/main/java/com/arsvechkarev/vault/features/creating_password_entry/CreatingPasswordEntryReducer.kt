package com.arsvechkarev.vault.features.creating_password_entry

import buisnesslogic.model.PasswordEntryData
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.RouterCommand.GoToCreatePasswordScreen
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.RouterCommand.GoToInfoScreen
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.SavePassword
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.ValidateInput
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.PasswordEntryCreated
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.SendValidationResult
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.ValidationResult.Fail
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.ValidationResult.Success
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryUiEvent.OnContinueClicked
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryUiEvent.OnTitleTextChanged
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryUiEvent.OnUsernameTextChanged
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryUiEvent.PasswordEntered

class CreatingPasswordEntryReducer :
  DslReducer<CreatingPasswordEntryState, CreatingPasswordEntryEvent,
      CreatingPasswordEntryCommand, Nothing>() {
  
  override fun dslReduce(event: CreatingPasswordEntryEvent) {
    when (event) {
      is OnTitleTextChanged -> {
        state { copy(title = event.text, titleEmpty = false) }
      }
      
      is OnUsernameTextChanged -> {
        state { copy(username = event.text, usernameEmpty = false) }
      }
      
      is OnContinueClicked -> {
        commands(ValidateInput(event.title, event.username))
      }
      
      OnBackPressed -> {
        commands(GoBack)
      }
      
      is SendValidationResult -> {
        when (val result = event.validationResult) {
          is Fail -> {
            state {
              copy(
                titleEmpty = result.titleEmpty,
                usernameEmpty = result.usernameEmpty
              )
            }
          }
          
          Success -> {
            commands(GoToCreatePasswordScreen)
          }
        }
      }
      
      is PasswordEntered -> {
        val passwordEntryData = PasswordEntryData(
          title = state.title,
          username = state.username,
          password = event.password,
          url = "",
          notes = "",
        )
        commands(SavePassword(passwordEntryData))
      }
      
      is PasswordEntryCreated -> {
        commands(GoToInfoScreen(event.passwordId))
      }
    }
  }
}
