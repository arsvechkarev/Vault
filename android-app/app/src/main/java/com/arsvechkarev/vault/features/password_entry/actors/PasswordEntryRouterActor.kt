package com.arsvechkarev.vault.features.password_entry.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens.CreatingPasswordScreen
import com.arsvechkarev.vault.features.common.navigation.RouterActor
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.RouterCommand
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.RouterCommand.GoToCreatingPasswordScreen
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent

fun PasswordEntryRouterActor(router: Router): Actor<PasswordEntryCommand, PasswordEntryEvent> {
  return RouterActor<PasswordEntryCommand, RouterCommand, PasswordEntryEvent>(router) { command ->
    when (command) {
      is GoToCreatingPasswordScreen -> goForward(CreatingPasswordScreen)
      GoBack -> goBack()
    }
  }
}
