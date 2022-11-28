package com.arsvechkarev.vault.features.main_list.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens.CreatingEntryScreen
import com.arsvechkarev.vault.features.common.Screens.ExportPasswordsScreen
import com.arsvechkarev.vault.features.common.Screens.ImportPasswordsScreen
import com.arsvechkarev.vault.features.common.Screens.InfoScreen
import com.arsvechkarev.vault.features.common.Screens.SettingsScreen
import com.arsvechkarev.vault.features.common.presentation.RouterActor
import com.arsvechkarev.vault.features.main_list.MainListCommand
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.GoToInfoScreen
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.OpenMenuItem
import com.arsvechkarev.vault.features.main_list.MainListEvent
import com.arsvechkarev.vault.features.main_list.MenuItemType
import kotlinx.coroutines.delay

fun MainListRouterActor(router: Router): Actor<MainListCommand, MainListEvent> {
  return RouterActor<MainListCommand, RouterCommand, MainListEvent>(router) { command ->
    when (command) {
      is OpenMenuItem -> {
        delay(Durations.MenuOpening)
        when (command.item) {
          MenuItemType.EXPORT_PASSWORDS -> goForward(ExportPasswordsScreen)
          MenuItemType.IMPORT_PASSWORDS -> goForward(ImportPasswordsScreen)
          MenuItemType.SETTINGS -> goForward(SettingsScreen)
          MenuItemType.NEW_PASSWORD -> goForward(CreatingEntryScreen)
        }
      }
      is GoToInfoScreen -> {
        goForward(InfoScreen(command.passwordInfoItem))
      }
      GoBack -> {
        goBack()
      }
    }
  }
}
