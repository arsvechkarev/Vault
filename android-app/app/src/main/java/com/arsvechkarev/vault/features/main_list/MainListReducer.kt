package com.arsvechkarev.vault.features.main_list

import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.main_list.MainListCommand.LoadData
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.GoToInfoScreen
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.OpenMenuItem
import com.arsvechkarev.vault.features.main_list.MainListEvent.UpdateData
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnCloseMenuClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnEntryItemClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnInit
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnMenuItemClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnOpenMenuClicked

class MainListReducer : DslReducer<MainListState, MainListEvent, MainListCommand, Nothing>() {
  
  override fun dslReduce(event: MainListEvent) {
    when (event) {
      OnInit -> {
        commands(LoadData)
      }
      OnOpenMenuClicked -> {
        state { copy(menuOpened = true) }
      }
      OnCloseMenuClicked -> {
        state { copy(menuOpened = false) }
      }
      is OnMenuItemClicked -> {
        state { copy(menuOpened = false) }
        commands(OpenMenuItem(event.item))
      }
      is OnEntryItemClicked -> {
        commands(GoToInfoScreen(event.entryItem))
      }
      OnBackPressed -> {
        if (state.menuOpened) {
          state { copy(menuOpened = false) }
        } else {
          commands(GoBack)
        }
      }
      is UpdateData -> {
        state { copy(data = event.data) }
      }
    }
  }
}
