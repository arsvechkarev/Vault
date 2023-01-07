package com.arsvechkarev.vault.features.info.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens.CreatingPasswordScreen
import com.arsvechkarev.vault.features.common.navigation.RouterActor
import com.arsvechkarev.vault.features.info.InfoScreenCommand
import com.arsvechkarev.vault.features.info.InfoScreenCommand.RouterCommand
import com.arsvechkarev.vault.features.info.InfoScreenCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.info.InfoScreenCommand.RouterCommand.GoToCreatePasswordScreen
import com.arsvechkarev.vault.features.info.InfoScreenEvent

fun InfoRouterActor(router: Router): Actor<InfoScreenCommand, InfoScreenEvent> {
  return RouterActor<InfoScreenCommand, RouterCommand, InfoScreenEvent>(router) { command ->
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
