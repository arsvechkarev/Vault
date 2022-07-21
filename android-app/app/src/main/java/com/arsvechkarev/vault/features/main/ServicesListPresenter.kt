package com.arsvechkarev.vault.features.main

import buisnesslogic.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.core.CachedPasswordsStorage
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.core.Result
import com.arsvechkarev.vault.core.Router
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.model.PasswordInfoItem
import com.arsvechkarev.vault.core.mvi.BaseMviPresenter
import com.arsvechkarev.vault.features.main.MainScreenEvent.DeletedService
import com.arsvechkarev.vault.features.main.MainScreenEvent.UpdateData
import com.arsvechkarev.vault.features.main.MainScreenEvent.UpdateSettingsIcon
import com.arsvechkarev.vault.features.main.MainScreenUiEvent.HideDeleteDialog
import com.arsvechkarev.vault.features.main.MainScreenUiEvent.OnAgreeToDeleteServiceClicked
import com.arsvechkarev.vault.features.main.MainScreenUiEvent.OnFabClicked
import com.arsvechkarev.vault.features.main.MainScreenUiEvent.OnServiceItemClicked
import com.arsvechkarev.vault.features.main.MainScreenUiEvent.OnServiceItemLongClicked
import com.arsvechkarev.vault.features.main.MainScreenUiEvent.OnSettingsClicked
import com.arsvechkarev.vault.features.main.MainScreenUiEvent.StartInitialLoading
import kotlinx.coroutines.launch

class ServicesListPresenter constructor(
  private val servicesRepository: CachedPasswordsStorage,
  private val router: Router,
  dispatchers: DispatchersFacade
) : BaseMviPresenter<MainScreenEvent, MainScreenUiEvent, MainState>(
  MainScreenUiEvent::class,
  dispatchers
) {
  
  init {
    servicesRepository.servicesFlow.collectInPresenterScope { services ->
      if (services.isNotEmpty()) {
        applyAction(UpdateData(Result.success(services)))
      } else {
        applyAction(UpdateData(Result.empty()))
      }
    }
  }
  
  override fun onFirstViewAttach() {
    applyAction(StartInitialLoading)
  }
  
  override fun getDefaultState() = MainState()
  
  override fun reduce(action: MainScreenEvent) = when (action) {
    is StartInitialLoading -> {
      state.copy(result = Result.loading())
    }
    is UpdateData -> {
      state.copy(result = action.data)
    }
    is UpdateSettingsIcon -> {
      state.copy(showSettingsIcon = action.showSettingsIcon)
    }
    is OnServiceItemLongClicked -> {
      state.copy(deleteDialog = DeleteDialog(action.passwordInfoItem))
    }
    OnAgreeToDeleteServiceClicked -> {
      state.copy(showDeletionLoadingDialog = true)
    }
    HideDeleteDialog -> {
      state.copy(deleteDialog = null)
    }
    DeletedService -> {
      state.copy(showDeletionLoadingDialog = false)
    }
    else -> state
  }
  
  override fun onSideEffect(action: MainScreenUiEvent) {
    when (action) {
      StartInitialLoading -> {
        startLoadingPasswords()
      }
      OnSettingsClicked -> {
        if (state.showSettingsIcon) {
          router.goForward(Screens.SettingsScreen)
        }
      }
      OnFabClicked -> {
        router.goForward(Screens.CreatingServiceScreen)
      }
      is OnServiceItemClicked -> {
        router.goForward(Screens.InfoScreen(action.passwordInfoItem))
      }
      OnAgreeToDeleteServiceClicked -> {
        state.deleteDialog?.let { deleteService(it.passwordInfoItem) }
      }
      else -> Unit
    }
  }
  
  private fun deleteService(passwordInfoItem: PasswordInfoItem) {
    launch {
      applyAction(HideDeleteDialog)
      onIoThread {
        servicesRepository.deletePassword(masterPassword, passwordInfoItem,
          notifySubscribers = true)
      }
      applyAction(DeletedService)
    }
  }
  
  private fun startLoadingPasswords() {
    launch {
      val services = onIoThread { servicesRepository.getPasswords(masterPassword) }
      if (services.isNotEmpty()) {
        applyAction(UpdateData(Result.success(services)))
      } else {
        applyAction(UpdateData(Result.empty()))
      }
    }
  }
}
