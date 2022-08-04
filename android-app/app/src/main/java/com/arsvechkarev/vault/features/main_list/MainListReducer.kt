package com.arsvechkarev.vault.features.main_list

import android.util.Log
import com.arsvechkarev.vault.core.Router
import com.arsvechkarev.vault.core.Screens.CreatingServiceScreen
import com.arsvechkarev.vault.core.Screens.InfoScreen
import com.arsvechkarev.vault.core.Screens.SettingsScreen
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.main_list.MainListCommand.LoadData
import com.arsvechkarev.vault.features.main_list.MainListEvent.UpdateData
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnFabClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnInit
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnMenuItemClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnPasswordItemClicked
import com.arsvechkarev.vault.features.main_list.MenuItem.EXPORT
import com.arsvechkarev.vault.features.main_list.MenuItem.IMPORT
import com.arsvechkarev.vault.features.main_list.MenuItem.NEW
import com.arsvechkarev.vault.features.main_list.MenuItem.SETTINGS

class MainListReducer(
  private val router: Router
) : DslReducer<MainListState, MainListEvent, MainListCommand, Nothing>() {
  
  override fun dslReduce(event: MainListEvent) {
    Log.d("TeaStoreImplMainList", "processing event $event")
    when (event) {
      is MainListUiEvent -> handleUiEvent(event)
      is UpdateData -> {
        Log.d("TeaStoreImplMainList", "updating data to ${event.data}")
        state { copy(data = event.data) }
      }
    }
  }
  
  private fun handleUiEvent(event: MainListUiEvent) {
    Log.d("TeaStoreImplMainList", "processing ui event $event")
    when (event) {
      OnInit -> {
        Log.d("TeaStoreImplMainList", "issuing load data command")
        commands(LoadData)
      }
      OnFabClicked -> {
        state { copy(menuOpened = true) }
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
