package com.arsvechkarev.vault.features.services_list.presentation

import com.arsvechkarev.vault.core.model.ServiceModel
import com.arsvechkarev.vault.core.mvi.result.Result

sealed class ServicesListAction {
  
  class UpdateData(val data: Result<List<ServiceModel>>) : ServicesListAction()
  
  class UpdateSettingsIcon(val showSettingsIcon: Boolean) : ServicesListAction()
  
  object DeletedService : ServicesListAction()
}

sealed class ServicesListUserAction : ServicesListAction() {
  
  object StartInitialLoading : ServicesListUserAction()
  
  object OnSettingsClicked : ServicesListUserAction()
  
  object OnFabClicked : ServicesListUserAction()
  
  class OnServiceItemClicked(val serviceModel: ServiceModel) : ServicesListUserAction()
  
  class OnServiceItemLongClicked(val serviceModel: ServiceModel) : ServicesListUserAction()
  
  object HideDeleteDialog : ServicesListUserAction()
  
  object OnAgreeToDeleteServiceClicked : ServicesListUserAction()
}

data class ServicesListState(
  val result: Result<List<ServiceModel>>? = null,
  val showSettingsIcon: Boolean = false,
  val deleteDialog: DeleteDialog? = null,
  val showDeletionLoadingDialog: Boolean = false
)

data class DeleteDialog(val serviceModel: ServiceModel)