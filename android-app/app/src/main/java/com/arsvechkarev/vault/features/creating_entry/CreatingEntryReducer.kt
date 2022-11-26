package com.arsvechkarev.vault.features.creating_entry

import com.arsvechkarev.vault.core.Router
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand.NotifyEntryCreated
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand.SaveEntry
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand.ValidateInput
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent.EntryCreated
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent.PasswordEntered
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent.SendValidationResult
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent.ValidationResult.Fail
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent.ValidationResult.Success
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryUiEvent.OnBackButtonClicked
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryUiEvent.OnContinueClicked
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryUiEvent.OnLoginTextChanged
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryUiEvent.OnWebsiteNameTextChanged

class CreatingEntryReducer(
  private val router: Router
) : DslReducer<CreatingEntryState, CreatingEntryEvent, CreatingEntryCommand, Nothing>() {
  
  override fun dslReduce(event: CreatingEntryEvent) {
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
      OnBackButtonClicked -> {
        router.goBack()
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
            router.goForward(Screens.CreatingPasswordScreen)
          }
        }
      }
      is PasswordEntered -> {
        commands(SaveEntry(state.websiteName, state.login, event.password))
      }
      is EntryCreated -> {
        commands(NotifyEntryCreated)
        router.goForwardWithRemovalOf(Screens.InfoScreen(event.passwordInfoItem), 2)
      }
    }
  }
}
