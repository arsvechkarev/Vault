package com.arsvechkarev.vault.features.main_list.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens.CreatingPasswordEntryScreen
import com.arsvechkarev.vault.features.common.Screens.ExportPasswordsScreen
import com.arsvechkarev.vault.features.common.Screens.ImportPasswordsScreen
import com.arsvechkarev.vault.features.common.Screens.PasswordInfoScreen
import com.arsvechkarev.vault.features.common.Screens.PlainTextScreen
import com.arsvechkarev.vault.features.common.Screens.SettingsScreen
import com.arsvechkarev.vault.features.common.model.CreditCardItem
import com.arsvechkarev.vault.features.common.model.PasswordItem
import com.arsvechkarev.vault.features.common.model.PlainTextItem
import com.arsvechkarev.vault.features.common.navigation.RouterActor
import com.arsvechkarev.vault.features.main_list.MainListCommand
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.GoToCorrespondingInfoScreen
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.OpenScreen
import com.arsvechkarev.vault.features.main_list.MainListEvent
import com.arsvechkarev.vault.features.main_list.ScreenType
import kotlinx.coroutines.delay

fun MainListRouterActor(router: Router): Actor<MainListCommand, MainListEvent> {
  return RouterActor<MainListCommand, RouterCommand, MainListEvent>(router) { command ->
    when (command) {
      is OpenScreen -> {
        delay(Durations.MenuOpening)
        when (command.type) {
          ScreenType.EXPORT_PASSWORDS -> goForward(ExportPasswordsScreen)
          ScreenType.IMPORT_PASSWORDS -> goForward(ImportPasswordsScreen)
          ScreenType.SETTINGS -> goForward(SettingsScreen)
          ScreenType.NEW_PASSWORD -> goForward(CreatingPasswordEntryScreen)
          ScreenType.NEW_CREDIT_CARD -> TODO()
          ScreenType.NEW_PLAIN_TEXT -> goForward(PlainTextScreen())
        }
      }
      
      is GoToCorrespondingInfoScreen -> {
        when (command.entryItem) {
          is PasswordItem -> goForward(PasswordInfoScreen(command.entryItem))
          is CreditCardItem -> TODO()
          is PlainTextItem -> goForward(PlainTextScreen(command.entryItem))
        }
      }
      
      GoBack -> {
        goBack()
      }
    }
  }
}
