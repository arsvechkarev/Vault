package com.arsvechkarev.vault.features.main_list

import com.arsvechkarev.vault.core.ScreenState
import com.arsvechkarev.vault.core.model.EntryItem
import com.arsvechkarev.vault.features.main_list.model.EntriesItems

sealed interface MainListEvent {
  class UpdateData(val data: ScreenState<EntriesItems>) : MainListEvent
}

sealed interface MainListUiEvent : MainListEvent {
  object OnInit : MainListUiEvent
  object OnOpenMenuClicked : MainListUiEvent
  object OnCloseMenuClicked : MainListUiEvent
  object OnBackPressed : MainListUiEvent
  class OnEntryItemClicked(val entryItem: EntryItem) : MainListUiEvent
  class OnMenuItemClicked(val item: MenuItemType) : MainListUiEvent
}

sealed interface MainListCommand {
  
  object LoadData : MainListCommand
  
  sealed interface RouterCommand : MainListCommand {
    class OpenMenuItem(val item: MenuItemType) : RouterCommand
    class GoToInfoScreen(val entryItem: EntryItem) : RouterCommand
    object GoBack : RouterCommand
  }
}

data class MainListState(
  val data: ScreenState<EntriesItems> = ScreenState.loading(),
  val menuOpened: Boolean = false,
)

enum class MenuItemType {
  IMPORT_PASSWORDS,
  EXPORT_PASSWORDS,
  SETTINGS,
  NEW_PASSWORD
}
