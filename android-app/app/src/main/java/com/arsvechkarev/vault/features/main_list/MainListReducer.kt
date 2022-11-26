package com.arsvechkarev.vault.features.main_list

import com.arsvechkarev.vault.core.Router
import com.arsvechkarev.vault.core.Screens.CreatingEntryScreen
import com.arsvechkarev.vault.core.Screens.ExportPasswordsScreen
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
import com.arsvechkarev.vault.features.main_list.MenuItemType.EXPORT_PASSWORDS
import com.arsvechkarev.vault.features.main_list.MenuItemType.IMPORT_PASSWORDS
import com.arsvechkarev.vault.features.main_list.MenuItemType.NEW_PASSWORD
import com.arsvechkarev.vault.features.main_list.MenuItemType.SETTINGS
import config.DurationsConfigurator

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
        state { copy(menuOpened = false) }
        when (event.itemType) {
          EXPORT_PASSWORDS -> {
            router.goForwardWithDelay(ExportPasswordsScreen, DurationsConfigurator.MenuOpening)
          }
          IMPORT_PASSWORDS -> {
            TODO("Add import passwords screen")
          }
          SETTINGS -> {
            // TODO (8/12/2022): Figure out closing menu with animating to next screen.
            // Options: - Closing with no animation
            //          - Using news ?
            //          - Not cancelling scope when moving to next screen
            router.goForwardWithDelay(SettingsScreen, DurationsConfigurator.MenuOpening)
          }
          NEW_PASSWORD -> {
            router.goForwardWithDelay(CreatingEntryScreen, DurationsConfigurator.MenuOpening)
          }
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
