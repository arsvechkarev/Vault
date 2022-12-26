package com.arsvechkarev.vault.features.main_list

import com.arsvechkarev.vault.core.ScreenState
import com.arsvechkarev.vault.core.model.PasswordInfoItem

sealed interface MainListEvent {
  class UpdateData(val data: ScreenState<List<PasswordInfoItem>>) : MainListEvent
}

sealed interface MainListUiEvent : MainListEvent {
  object OnInit : MainListUiEvent
  object OnOpenMenuClicked : MainListUiEvent
  object OnCloseMenuClicked : MainListUiEvent
  object OnBackPressed : MainListUiEvent
  class OnPasswordItemClicked(val passwordInfoItem: PasswordInfoItem) : MainListUiEvent
  class OnMenuItemClicked(val item: MenuItemType) : MainListUiEvent
}

sealed interface MainListCommand {
  
  object LoadData : MainListCommand
  
  sealed interface RouterCommand : MainListCommand {
    class OpenMenuItem(val item: MenuItemType) : RouterCommand
    class GoToInfoScreen(val passwordInfoItem: PasswordInfoItem) : RouterCommand
    object GoBack : RouterCommand
  }
}

data class MainListState(
  val data: ScreenState<List<PasswordInfoItem>> = ScreenState.loading(),
  val menuOpened: Boolean = false,
)

enum class MenuItemType {
  IMPORT_PASSWORDS,
  EXPORT_PASSWORDS,
  SETTINGS,
  NEW_PASSWORD
}
