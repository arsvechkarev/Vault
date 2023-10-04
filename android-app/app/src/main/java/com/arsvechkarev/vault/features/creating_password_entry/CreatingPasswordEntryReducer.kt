package com.arsvechkarev.vault.features.creating_password_entry

import buisnesslogic.model.PasswordEntryData
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.RouterCommand.GoToCreatePasswordScreen
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.RouterCommand.GoToInfoScreen
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.SavePassword
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent.PasswordEntryCreated
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
        state { copy(title = event.text, showTitleEmptyError = false) }
      }
      
      is OnUsernameTextChanged -> {
        state { copy(username = event.text) }
      }
      
      OnContinueClicked -> {
        if (state.title.isNotBlank()) {
          commands(GoToCreatePasswordScreen)
        } else {
          state { copy(showTitleEmptyError = true) }
        }
      }
      
      OnBackPressed -> {
        commands(GoBack)
      }
      
      is PasswordEntered -> {
        val passwordEntryData = PasswordEntryData(
          title = state.title,
          username = state.username,
          password = event.password,
          url = "",
          notes = "",
          isFavorite = false
        )
        commands(SavePassword(passwordEntryData))
      }
      
      is PasswordEntryCreated -> {
        commands(GoToInfoScreen(event.passwordId))
      }
    }
  }
}
