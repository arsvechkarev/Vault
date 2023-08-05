package com.arsvechkarev.vault.features.main_list

import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.main_list.MainListCommand.LoadData
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.GoToCorrespondingInfoScreen
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.OpenScreen
import com.arsvechkarev.vault.features.main_list.MainListEvent.UpdateData
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnCloseMenuClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnEntryTypeDialogHidden
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnEntryTypeSelected
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnInit
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnListItemClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnMenuItemClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnOpenMenuClicked
import com.arsvechkarev.vault.features.main_list.ScreenType.EXPORT_PASSWORDS
import com.arsvechkarev.vault.features.main_list.ScreenType.IMPORT_PASSWORDS
import com.arsvechkarev.vault.features.main_list.ScreenType.SETTINGS

class MainListReducer : DslReducer<MainListState, MainListEvent, MainListCommand, Nothing>() {
  
  override fun dslReduce(event: MainListEvent) {
    when (event) {
      OnInit -> {
        commands(LoadData)
      }
      
      is OnListItemClicked -> {
        commands(GoToCorrespondingInfoScreen(event.item))
      }
      
      OnOpenMenuClicked -> {
        state { copy(menuOpened = true) }
      }
      
      OnCloseMenuClicked -> {
        state { copy(menuOpened = false) }
      }
      
      is OnMenuItemClicked -> {
        if (event.itemType != MenuItemType.NEW_ENTRY) {
          state { copy(menuOpened = false) }
        }
        when (event.itemType) {
          MenuItemType.IMPORT_PASSWORDS -> commands(OpenScreen(IMPORT_PASSWORDS))
          MenuItemType.EXPORT_PASSWORDS -> commands(OpenScreen(EXPORT_PASSWORDS))
          MenuItemType.SETTINGS -> commands(OpenScreen(SETTINGS))
          MenuItemType.NEW_ENTRY -> state { copy(showEntryTypeDialog = true) }
        }
      }
      
      is OnEntryTypeSelected -> {
        state { copy(showEntryTypeDialog = false, menuOpened = false) }
        val screenType = when (event.type) {
          EntryType.PASSWORD -> ScreenType.NEW_PASSWORD
          EntryType.CREDIT_CARD -> ScreenType.NEW_CREDIT_CARD
          EntryType.PLAIN_TEXT -> ScreenType.NEW_PLAIN_TEXT
        }
        commands(OpenScreen(screenType))
      }
      
      OnEntryTypeDialogHidden -> {
        state { copy(showEntryTypeDialog = false) }
      }
      
      OnBackPressed -> {
        when {
          state.showEntryTypeDialog -> state { copy(showEntryTypeDialog = false) }
          state.menuOpened -> state { copy(menuOpened = false) }
          else -> commands(GoBack)
        }
      }
      
      is UpdateData -> {
        state { copy(data = event.data) }
      }
    }
  }
}
