package com.arsvechkarev.vault.features.login.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens.MainListScreen
import com.arsvechkarev.vault.features.common.navigation.RouterActor
import com.arsvechkarev.vault.features.login.LoginCommand
import com.arsvechkarev.vault.features.login.LoginCommand.GoToMainListScreen
import com.arsvechkarev.vault.features.login.LoginEvent

fun LoginRouterActor(router: Router): Actor<LoginCommand, LoginEvent> {
  return RouterActor<LoginCommand, GoToMainListScreen, LoginEvent>(router) {
    switchToNewRoot(MainListScreen)
  }
}
