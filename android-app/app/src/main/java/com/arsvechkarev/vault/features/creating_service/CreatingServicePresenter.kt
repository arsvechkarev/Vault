package com.arsvechkarev.vault.features.creating_service

import buisnesslogic.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.core.CachedPasswordsStorage
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.core.Router
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.communicators.FlowCommunicator
import com.arsvechkarev.vault.core.model.PasswordInfoItem
import com.arsvechkarev.vault.core.mvi.BaseMviPresenter
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ConfigureMode.NewPassword
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ExitScreen
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ShowAcceptPasswordDialog
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ShowLoading
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingCommunicator
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingEvents
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingReactions.OnNewPasswordAccepted
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingReactions.OnSavePasswordButtonClicked
import com.arsvechkarev.vault.features.creating_service.CreatingServiceActions.ShowServiceNameCannotBeEmpty
import com.arsvechkarev.vault.features.creating_service.CreatingServiceUserActions.OnBackPressed
import com.arsvechkarev.vault.features.creating_service.CreatingServiceUserActions.OnContinueClicked
import com.arsvechkarev.vault.features.creating_service.CreatingServiceUserActions.OnServiceNameTextChanged
import kotlinx.coroutines.launch
import java.util.UUID

class CreatingServicePresenter constructor(
  @PasswordCreatingCommunicator
  private val passwordCreatingCommunicator: FlowCommunicator<PasswordCreatingEvents>,
  private val servicesRepository: CachedPasswordsStorage,
  private val router: Router,
  dispatchers: DispatchersFacade
) : BaseMviPresenter<CreatingServiceActions, CreatingServiceUserActions, CreatingServiceState>(
  CreatingServiceUserActions::class,
  dispatchers
) {
  
  init {
    subscribeToPasswordCreatingEvents()
  }
  
  override fun getDefaultState(): CreatingServiceState {
    return CreatingServiceState()
  }
  
  override fun reduce(action: CreatingServiceActions) = when (action) {
    is OnServiceNameTextChanged -> {
      state.copy(serviceName = action.text, showServiceIconCannotBeEmpty = false)
    }
    ShowServiceNameCannotBeEmpty -> {
      state.copy(showServiceIconCannotBeEmpty = true)
    }
    is OnContinueClicked -> {
      state.copy(
        serviceName = action.serviceName.trim(),
        username = action.username.trim(),
        email = action.email.trim()
      )
    }
    else -> state
  }
  
  override fun onSideEffect(action: CreatingServiceUserActions) {
    when (action) {
      OnBackPressed -> {
        router.goBack()
      }
      is OnContinueClicked -> {
        onContinueClicked()
      }
      else -> Unit
    }
  }
  
  private fun onContinueClicked() {
    if (state.serviceName.isBlank()) {
      applyAction(ShowServiceNameCannotBeEmpty)
      return
    }
    launch { passwordCreatingCommunicator.send(NewPassword) }
    router.goForward(Screens.PasswordCreatingScreen)
  }
  
  private fun performServiceSaving(password: String) {
    launch {
      passwordCreatingCommunicator.send(ShowLoading)
      val serviceInfo = PasswordInfoItem(
        UUID.randomUUID().toString(), state.serviceName, state.username, state.email, password
      )
      onIoThread { servicesRepository.savePassword(masterPassword, serviceInfo) }
      passwordCreatingCommunicator.send(ExitScreen)
      router.goBackTo(Screens.ServicesListScreen)
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
