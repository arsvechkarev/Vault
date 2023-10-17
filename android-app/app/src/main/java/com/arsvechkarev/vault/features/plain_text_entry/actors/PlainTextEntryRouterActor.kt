package com.arsvechkarev.vault.features.plain_text_entry.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens
import com.arsvechkarev.vault.features.common.navigation.RouterActor
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryCommand
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryCommand.RouterCommand
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryCommand.RouterCommand.SwitchBackToLogin
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryEvent

fun PlainTextRouterActor(
  router: Router
): Actor<PlainTextEntryCommand, PlainTextEntryEvent> {
  return RouterActor<PlainTextEntryCommand, RouterCommand, PlainTextEntryEvent>(router) { command ->
    when (command) {
      GoBack -> goBack()
      SwitchBackToLogin -> switchToNewRoot(Screens.LoginScreen)
    }
  }
}