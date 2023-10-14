package com.arsvechkarev.vault.features.main_list.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens.ImportPasswordsScreen
import com.arsvechkarev.vault.features.common.Screens.PasswordEntryScreen
import com.arsvechkarev.vault.features.common.Screens.PlainTextScreen
import com.arsvechkarev.vault.features.common.Screens.SettingsScreen
import com.arsvechkarev.vault.features.common.navigation.RouterActor
import com.arsvechkarev.vault.features.main_list.MainListCommand
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.OpenScreen
import com.arsvechkarev.vault.features.main_list.MainListEvent
import com.arsvechkarev.vault.features.main_list.ScreenInfo.ImportPasswords
import com.arsvechkarev.vault.features.main_list.ScreenInfo.NewPassword
import com.arsvechkarev.vault.features.main_list.ScreenInfo.NewPlainText
import com.arsvechkarev.vault.features.main_list.ScreenInfo.Password
import com.arsvechkarev.vault.features.main_list.ScreenInfo.PlainText
import com.arsvechkarev.vault.features.main_list.ScreenInfo.Settings
import kotlinx.coroutines.delay
import navigation.ScreenInfo

fun MainListRouterActor(router: Router): Actor<MainListCommand, MainListEvent> {
  return RouterActor<MainListCommand, RouterCommand, MainListEvent>(router) { command ->
    when (command) {
      is OpenScreen -> {
        when (val info = command.info) {
          is Password -> goForward(PasswordEntryScreen(info.passwordEntry.id))
          is PlainText -> goForward(PlainTextScreen(info.plainTextEntry.id))
          is ImportPasswords -> goForward(ImportPasswordsScreen(info.selectedFileUri),
            animate = false)
          NewPassword -> goForwardWithDelay(PasswordEntryScreen())
          NewPlainText -> goForwardWithDelay(PlainTextScreen())
          Settings -> goForwardWithDelay(SettingsScreen)
        }
      }
      GoBack -> {
        goBack()
      }
    }
  }
}

private suspend fun Router.goForwardWithDelay(screenInfo: ScreenInfo) {
  delay(Durations.MenuOpening)
  goForward(screenInfo)
}
