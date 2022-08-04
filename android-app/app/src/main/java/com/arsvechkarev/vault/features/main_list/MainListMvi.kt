package com.arsvechkarev.vault.features.main_list

import com.arsvechkarev.vault.core.State
import com.arsvechkarev.vault.core.model.PasswordInfoItem

sealed interface MainListEvent {
  class UpdateData(val data: State<List<PasswordInfoItem>>) : MainListEvent
}

sealed interface MainListUiEvent : MainListEvent {
  object OnInit : MainListUiEvent
  object OnFabClicked : MainListUiEvent
  object OnBackPressed : MainListUiEvent
  class OnPasswordItemClicked(val passwordInfoItem: PasswordInfoItem) : MainListUiEvent
  class OnMenuItemClicked(val menuItem: MenuItem) : MainListUiEvent
}

sealed interface MainListCommand {
  object LoadData : MainListCommand
}

data class MainListState(
  val data: State<List<PasswordInfoItem>>? = null,
  val menuOpened: Boolean = false,
)

enum class MenuItem {
  EXPORT, IMPORT, SETTINGS, NEW
}
