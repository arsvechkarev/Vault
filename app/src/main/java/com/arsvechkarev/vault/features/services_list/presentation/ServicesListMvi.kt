package com.arsvechkarev.vault.features.services_list.presentation

import com.arsvechkarev.vault.core.Result
import com.arsvechkarev.vault.core.model.ServiceModel

sealed class ServicesListScreenAction {
  
  class UpdateData(val data: Result<List<ServiceModel>>) : ServicesListScreenAction()
  
  class UpdateSettingsIcon(val showSettingsIcon: Boolean) : ServicesListScreenAction()
  
  object DeletedService : ServicesListScreenAction()
}

sealed class ServicesListScreenUserAction : ServicesListScreenAction() {
  
  object StartInitialLoading : ServicesListScreenUserAction()
  
  object OnSettingsClicked : ServicesListScreenUserAction()
  
  object OnFabClicked : ServicesListScreenUserAction()
  
  class OnServiceItemClicked(val serviceModel: ServiceModel) : ServicesListScreenUserAction()
  
  class OnServiceItemLongClicked(val serviceModel: ServiceModel) : ServicesListScreenUserAction()
  
  object HideDeleteDialog : ServicesListScreenUserAction()
  
  object OnAgreeToDeleteServiceClicked : ServicesListScreenUserAction()
}

data class ServicesListState(
  val result: Result<List<ServiceModel>>? = null,
  val showSettingsIcon: Boolean = false,
  val deleteDialog: DeleteDialog? = null,
  val showDeletionLoadingDialog: Boolean = false
)

data class DeleteDialog(val serviceModel: ServiceModel)