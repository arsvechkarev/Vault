package com.arsvechkarev.vault.features.password_info.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens.CreatingPasswordScreen
import com.arsvechkarev.vault.features.common.navigation.RouterActor
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.RouterCommand
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.RouterCommand.GoToCreatePasswordScreen
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent

fun PasswordInfoRouterActor(router: Router): Actor<InfoScreenCommand, PasswordInfoScreenEvent> {
  return RouterActor<InfoScreenCommand, RouterCommand, PasswordInfoScreenEvent>(router) { command ->
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
