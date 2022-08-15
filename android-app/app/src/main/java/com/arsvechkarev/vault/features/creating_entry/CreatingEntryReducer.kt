package com.arsvechkarev.vault.features.creating_entry

import com.arsvechkarev.vault.core.Router
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand.ValidateInput
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryUiEvent.OnBackButtonClicked
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryUiEvent.OnContinueClicked
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryUiEvent.OnWebsiteNameTextChanged

class CreatingEntryReducer(
  private val router: Router
) : DslReducer<CreatingEntryState, CreatingEntryEvent, CreatingEntryCommand, Nothing>() {
  
  override fun dslReduce(event: CreatingEntryEvent) {
    when (event) {
      is OnWebsiteNameTextChanged -> {
        state { copy(websiteName = event.text) }
      }
      is OnContinueClicked -> {
        commands(ValidateInput(event.websiteName, event.login))
      }
      OnBackButtonClicked -> {
        router.goBack()
      }
      is CreatingEntryEvent.SendValidationResult -> {
        if (event.isSuccessful) {
          router.goForward(Screens.PasswordCreatingScreen)
        }
      }
    }
  }
}