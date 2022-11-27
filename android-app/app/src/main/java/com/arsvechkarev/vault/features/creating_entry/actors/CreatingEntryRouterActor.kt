package com.arsvechkarev.vault.features.creating_entry.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens.CreatingPasswordScreen
import com.arsvechkarev.vault.features.common.Screens.InfoScreen
import com.arsvechkarev.vault.features.common.presentation.RouterActor
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand.RouterCommand
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand.RouterCommand.GoToCreatePasswordScreen
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand.RouterCommand.GoToInfoScreen
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent

fun CreatingEntryRouterActor(router: Router): Actor<CreatingEntryCommand, CreatingEntryEvent> {
  return RouterActor<CreatingEntryCommand, RouterCommand, CreatingEntryEvent>(router) { command ->
    when (command) {
      GoToCreatePasswordScreen -> {
        goForward(CreatingPasswordScreen)
      }
      is GoToInfoScreen -> {
        goForwardWithRemovalOf(InfoScreen(command.passwordInfoItem), 2)
      }
      GoBack -> {
        goBack()
      }
    }
  }
}
