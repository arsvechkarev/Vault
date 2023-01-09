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
  object OnChooseEntryTypeDialogHidden : MainListUiEvent
  object OnBackPressed : MainListUiEvent
  class OnListItemClicked(val item: EntryItem) : MainListUiEvent
  class OnMenuItemClicked(val itemType: MenuItemType) : MainListUiEvent
  class OnEntryTypeSelected(val type: EntryType) : MainListUiEvent
}

sealed interface MainListCommand {
  
  object LoadData : MainListCommand
  
  sealed interface RouterCommand : MainListCommand {
    class OpenScreen(val type: ScreenType) : RouterCommand
    class GoToCorrespondingInfoScreen(val entryItem: EntryItem) : RouterCommand
    object GoBack : RouterCommand
  }
}

data class MainListState(
  val data: ScreenState<EntriesItems> = ScreenState.loading(),
  val menuOpened: Boolean = false,
  val showEntryTypeDialog: Boolean = false,
)

enum class MenuItemType {
  IMPORT_PASSWORDS,
  EXPORT_PASSWORDS,
  SETTINGS,
  NEW_ENTRY,
}

enum class ScreenType {
  IMPORT_PASSWORDS,
  EXPORT_PASSWORDS,
  SETTINGS,
  NEW_PASSWORD,
  NEW_CREDIT_CARD,
  NEW_PLAIN_TEXT,
}

enum class EntryType {
  PASSWORD,
  CREDIT_CARD,
  PLAIN_TEXT
}
