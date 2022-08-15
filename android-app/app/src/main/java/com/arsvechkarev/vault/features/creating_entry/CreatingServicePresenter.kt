package com.arsvechkarev.vault.features.creating_entry

import buisnesslogic.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.core.CachedPasswordsStorage
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.core.Router
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.communicators.FlowCommunicator
import com.arsvechkarev.vault.core.model.PasswordInfoItem
import com.arsvechkarev.vault.core.mvi.BaseMviPresenter
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryUiEvent.OnBackButtonClicked
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryUiEvent.OnContinueClicked
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryUiEvent.OnWebsiteNameTextChanged
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ConfigureMode.NewPassword
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ExitScreen
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ShowAcceptPasswordDialog
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ShowLoading
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingCommunicator
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingEvents
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingReactions.OnNewPasswordAccepted
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingReactions.OnSavePasswordButtonClicked
import kotlinx.coroutines.launch
import java.util.UUID

class CreatingServicePresenter constructor(
  @PasswordCreatingCommunicator
  private val passwordCreatingCommunicator: FlowCommunicator<PasswordCreatingEvents>,
  private val servicesRepository: CachedPasswordsStorage,
  private val router: Router,
  dispatchers: DispatchersFacade
) : BaseMviPresenter<CreatingEntryEvent, CreatingEntryUiEvent, CreatingEntryState>(
  CreatingEntryUiEvent::class,
  dispatchers
) {
  
  init {
    subscribeToPasswordCreatingEvents()
  }
  
  override fun getDefaultState(): CreatingEntryState {
    return CreatingEntryState()
  }
  
  override fun reduce(action: CreatingEntryEvent) = when (action) {
    is OnWebsiteNameTextChanged -> {
      state.copy(websiteName = action.text)
    }
    //    ShowServiceNameCannotBeEmpty -> {
    //      state.copy()
    //    }
    is OnContinueClicked -> {
      state.copy(
        websiteName = action.websiteName.trim(),
        login = action.login.trim(),
        //        email = action.email.trim()
      )
    }
    else -> state
  }
  
  override fun onSideEffect(action: CreatingEntryUiEvent) {
    when (action) {
      OnBackButtonClicked -> {
        router.goBack()
      }
      is OnContinueClicked -> {
        onContinueClicked()
      }
      else -> Unit
    }
  }
  
  private fun onContinueClicked() {
    if (state.websiteName.isBlank()) {
      //      applyAction(ShowServiceNameCannotBeEmpty)
      return
    }
    launch { passwordCreatingCommunicator.send(NewPassword) }
    router.goForward(Screens.PasswordCreatingScreen)
  }
  
  private fun performServiceSaving(password: String) {
    launch {
      passwordCreatingCommunicator.send(ShowLoading)
      val serviceInfo = PasswordInfoItem(
        UUID.randomUUID().toString(), state.websiteName, state.login, "state.email", password
      )
      onIoThread { servicesRepository.savePassword(masterPassword, serviceInfo) }
      passwordCreatingCommunicator.send(ExitScreen)
      router.goBackTo(Screens.MainListScreen)
    }
  }
  
  private fun subscribeToPasswordCreatingEvents() {
    passwordCreatingCommunicator.events.collectInPresenterScope { event ->
      when (event) {
        is OnSavePasswordButtonClicked -> {
          passwordCreatingCommunicator.send(ShowAcceptPasswordDialog)
        }
        is OnNewPasswordAccepted -> {
          performServiceSaving(event.password)
        }
      }
    }
  }
}
