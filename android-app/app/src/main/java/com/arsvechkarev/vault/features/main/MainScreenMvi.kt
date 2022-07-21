package com.arsvechkarev.vault.features.main

import com.arsvechkarev.vault.core.Result
import com.arsvechkarev.vault.core.model.PasswordInfoItem

sealed interface MainScreenEvent {
  class UpdateData(val data: Result<List<PasswordInfoItem>>) : MainScreenEvent
  class UpdateSettingsIcon(val showSettingsIcon: Boolean) : MainScreenEvent
  object DeletedService : MainScreenEvent
}

sealed interface MainScreenUiEvent : MainScreenEvent {
  object StartInitialLoading : MainScreenUiEvent
  object OnSettingsClicked : MainScreenUiEvent
  object OnFabClicked : MainScreenUiEvent
  class OnServiceItemClicked(val passwordInfoItem: PasswordInfoItem) : MainScreenUiEvent
  class OnServiceItemLongClicked(val passwordInfoItem: PasswordInfoItem) : MainScreenUiEvent
  object HideDeleteDialog : MainScreenUiEvent
  object OnAgreeToDeleteServiceClicked : MainScreenUiEvent
}

data class MainState(
  val result: Result<List<PasswordInfoItem>>? = null,
  val showSettingsIcon: Boolean = false,
  val deleteDialog: DeleteDialog? = null,
  val showDeletionLoadingDialog: Boolean = false
)

data class DeleteDialog(val passwordInfoItem: PasswordInfoItem)
