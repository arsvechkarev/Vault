package com.arsvechkarev.vault.features.main_list.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens.ImportPasswordsScreen
import com.arsvechkarev.vault.features.common.Screens.LoginScreen
import com.arsvechkarev.vault.features.common.Screens.PasswordEntryScreen
import com.arsvechkarev.vault.features.common.Screens.NoteScreen
import com.arsvechkarev.vault.features.common.Screens.SettingsScreen
import com.arsvechkarev.vault.features.common.navigation.RouterActor
import com.arsvechkarev.vault.features.main_list.MainListCommand
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.OpenScreen
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.SwitchBackToLogin
import com.arsvechkarev.vault.features.main_list.MainListEvent
import com.arsvechkarev.vault.features.main_list.ScreenInfo.ImportPasswords
import com.arsvechkarev.vault.features.main_list.ScreenInfo.NewPassword
import com.arsvechkarev.vault.features.main_list.ScreenInfo.NewNote
import com.arsvechkarev.vault.features.main_list.ScreenInfo.Password
import com.arsvechkarev.vault.features.main_list.ScreenInfo.Note
import com.arsvechkarev.vault.features.main_list.ScreenInfo.Settings
import kotlinx.coroutines.delay
import navigation.ScreenInfo

fun MainListRouterActor(router: Router): Actor<MainListCommand, MainListEvent> {
  return RouterActor<MainListCommand, RouterCommand, MainListEvent>(router) { command ->
    when (command) {
      is OpenScreen -> {
        when (val info = command.info) {
          is Password -> goForward(PasswordEntryScreen(info.passwordEntry.id))
          is Note -> goForward(NoteScreen(info.noteEntry.id))
          is ImportPasswords -> {
            goForward(
              screenInfo = ImportPasswordsScreen(
                info.selectedFileUri,
                askForConfirmation = info.askForConfirmation
              ),
              animate = false
            )
          }
          NewPassword -> goForwardWithDelay(PasswordEntryScreen())
          NewNote -> goForwardWithDelay(NoteScreen())
          Settings -> goForwardWithDelay(SettingsScreen)
        }
      }
      GoBack -> {
        goBack()
      }
      SwitchBackToLogin -> {
        switchToNewRoot(LoginScreen)
      }
    }
  }
}

private suspend fun Router.goForwardWithDelay(screenInfo: ScreenInfo) {
  delay(Durations.MenuOpening)
  goForward(screenInfo)
}
