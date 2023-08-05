package com.arsvechkarev.vault.features.password_info.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens.CreatingPasswordScreen
import com.arsvechkarev.vault.features.common.navigation.RouterActor
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.RouterCommand
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.RouterCommand.GoToCreatePasswordScreen
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent

fun PasswordInfoRouterActor(router: Router): Actor<PasswordInfoScreenCommand, PasswordInfoScreenEvent> {
  return RouterActor<PasswordInfoScreenCommand, RouterCommand, PasswordInfoScreenEvent>(
    router) { command ->
    when (command) {
      GoToCreatePasswordScreen -> {
        goForward(CreatingPasswordScreen)
      }
      
      GoBack -> {
        goBack()
      }
    }
  }
}
