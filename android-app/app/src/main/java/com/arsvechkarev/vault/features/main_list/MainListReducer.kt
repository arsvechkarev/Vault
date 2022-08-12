package com.arsvechkarev.vault.features.main_list

import com.arsvechkarev.vault.core.Router
import com.arsvechkarev.vault.core.Screens.CreatingServiceScreen
import com.arsvechkarev.vault.core.Screens.InfoScreen
import com.arsvechkarev.vault.core.Screens.SettingsScreen
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.main_list.MainListCommand.LoadData
import com.arsvechkarev.vault.features.main_list.MainListEvent.UpdateData
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnCloseMenuClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnInit
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnMenuItemClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnOpenMenuClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnPasswordItemClicked
import com.arsvechkarev.vault.features.main_list.MenuItem.EXPORT
import com.arsvechkarev.vault.features.main_list.MenuItem.IMPORT
import com.arsvechkarev.vault.features.main_list.MenuItem.NEW
import com.arsvechkarev.vault.features.main_list.MenuItem.SETTINGS

class MainListReducer(
  private val router: Router
) : DslReducer<MainListState, MainListEvent, MainListCommand, Nothing>() {
  
  override fun dslReduce(event: MainListEvent) {
    when (event) {
      is MainListUiEvent -> handleUiEvent(event)
      is UpdateData -> {
        state { copy(data = event.data) }
      }
    }
  }
  
  private fun handleUiEvent(event: MainListUiEvent) {
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
        when (event.menuItem) {
          EXPORT -> TODO("Add export passwords screen")
          IMPORT -> TODO("Add import passwords screen")
          SETTINGS -> router.goForward(SettingsScreen)
          NEW -> router.goForward(CreatingServiceScreen)
        }
      }
      is OnPasswordItemClicked -> {
        router.goForward(InfoScreen(event.passwordInfoItem))
      }
      OnBackPressed -> {
        if (state.menuOpened) {
          state { copy(menuOpened = false) }
        } else {
          router.goBack()
        }
      }
    }
  }
}
