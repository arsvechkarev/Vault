package com.arsvechkarev.vault.features.services_list.presentation

import buisnesslogic.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.core.CoroutineRouter
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.core.Result
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.ServicesListenableRepository
import com.arsvechkarev.vault.core.model.ServiceModel
import com.arsvechkarev.vault.core.mvi.BaseMviPresenter
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListScreenAction.DeletedService
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListScreenAction.UpdateData
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListScreenAction.UpdateSettingsIcon
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListScreenUserAction.HideDeleteDialog
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListScreenUserAction.OnAgreeToDeleteServiceClicked
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListScreenUserAction.OnFabClicked
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListScreenUserAction.OnServiceItemClicked
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListScreenUserAction.OnServiceItemLongClicked
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListScreenUserAction.OnSettingsClicked
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListScreenUserAction.StartInitialLoading
import kotlinx.coroutines.launch
import javax.inject.Inject

class ServicesListPresenter @Inject constructor(
  private val servicesRepository: ServicesListenableRepository,
  private val router: CoroutineRouter,
  dispatchers: DispatchersFacade
) : BaseMviPresenter<ServicesListScreenAction, ServicesListScreenUserAction, ServicesListState>(
  ServicesListScreenUserAction::class,
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
  
  override fun getDefaultState() = ServicesListState()
  
  override fun reduce(action: ServicesListScreenAction) = when (action) {
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
      state.copy(deleteDialog = DeleteDialog(action.serviceModel))
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
  
  override fun onSideEffect(action: ServicesListScreenUserAction) {
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
        router.goForward(Screens.InfoScreen(action.serviceModel))
      }
      OnAgreeToDeleteServiceClicked -> {
        state.deleteDialog?.let { deleteService(it.serviceModel) }
      }
      else -> Unit
    }
  }
  
  private fun deleteService(serviceModel: ServiceModel) {
    launch {
      applyAction(HideDeleteDialog)
      onIoThread {
        servicesRepository.deleteService(masterPassword, serviceModel, notifySubscribers = true)
      }
      applyAction(DeletedService)
    }
  }
  
  private fun startLoadingPasswords() {
    launch {
      val services = onIoThread { servicesRepository.getServices(masterPassword) }
      if (services.isNotEmpty()) {
        applyAction(UpdateData(Result.success(services)))
      } else {
        applyAction(UpdateData(Result.empty()))
      }
    }
  }
}
