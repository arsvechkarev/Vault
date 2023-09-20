@file:Suppress("FunctionName")

package com.arsvechkarev.vault.features.creating_password_entry.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens.CreatingPasswordScreen
import com.arsvechkarev.vault.features.common.Screens.PasswordInfoScreen
import com.arsvechkarev.vault.features.common.navigation.RouterActor
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.RouterCommand
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.RouterCommand.GoToCreatePasswordScreen
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryCommand.RouterCommand.GoToInfoScreen
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryEvent

fun CreatingEntryRouterActor(router: Router): Actor<CreatingPasswordEntryCommand, CreatingPasswordEntryEvent> {
  return RouterActor<CreatingPasswordEntryCommand, RouterCommand, CreatingPasswordEntryEvent>(
    router) { command ->
    when (command) {
      GoToCreatePasswordScreen -> {
        goForward(CreatingPasswordScreen)
      }
      
      is GoToInfoScreen -> {
        goForwardWithRemovalOf(PasswordInfoScreen(command.passwordId), 2)
      }
      
      GoBack -> {
        goBack()
      }
    }
  }
}
