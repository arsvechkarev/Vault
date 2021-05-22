package com.arsvechkarev.vault.features.services_list.presentation

import buisnesslogic.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.core.Dispatchers
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.core.model.ServiceModel
import com.arsvechkarev.vault.core.mvi.BaseMviPresenter
import com.arsvechkarev.vault.core.mvi.result.Result
import com.arsvechkarev.vault.features.common.Screens
import com.arsvechkarev.vault.features.common.ServicesListenableRepository
import com.arsvechkarev.vault.features.common.biometrics.BiometricsAvailabilityChecker
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListAction.DeletedService
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListAction.UpdateData
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListAction.UpdateSettingsIcon
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListUserAction.HideDeleteDialog
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListUserAction.OnAgreeToDeleteServiceClicked
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListUserAction.OnFabClicked
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListUserAction.OnServiceItemClicked
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListUserAction.OnServiceItemLongClicked
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListUserAction.OnSettingsClicked
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListUserAction.StartInitialLoading
import kotlinx.coroutines.launch
import navigation.Router
import javax.inject.Inject

@FeatureScope
class ServicesListPresenter @Inject constructor(
  private val servicesRepository: ServicesListenableRepository,
  biometricsAvailabilityChecker: BiometricsAvailabilityChecker,
  private val router: Router,
  dispatchers: Dispatchers
) : BaseMviPresenter<ServicesListAction, ServicesListUserAction, ServicesListState>(
  ServicesListUserAction::class,
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
    if (biometricsAvailabilityChecker.isBiometricsSupported()) {
      applyAction(UpdateSettingsIcon(showSettingsIcon = true))
    }
  }
  
  override fun onFirstViewAttach() {
    applyAction(StartInitialLoading)
  }
  
  override fun getDefaultState() = ServicesListState()
  
  override fun reduce(action: ServicesListAction) = when (action) {
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
      state.copy(deleteDialog = null, showDeletionLoadingDialog = true)
    }
    HideDeleteDialog -> {
      state.copy(deleteDialog = null)
    }
    DeletedService -> {
      state.copy(showDeletionLoadingDialog = false)
    }
    else -> state
  }
  
  override fun onSideEffect(action: ServicesListUserAction) {
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
  
  fun deleteService(serviceModel: ServiceModel) {
    launch {
      onIoThread {
        servicesRepository.deleteService(masterPassword, serviceModel, notifySubscribers = false)
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