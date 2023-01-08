package com.arsvechkarev.vault.features.import_passwords.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens
import com.arsvechkarev.vault.features.common.navigation.RouterActor
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsCommand
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsCommand.RouterCommand
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsCommand.RouterCommand.GoToMainListScreen
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsEvent

fun ImportPasswordsRouterActor(
  router: Router
): Actor<ImportPasswordsCommand, ImportPasswordsEvent> {
  return RouterActor<ImportPasswordsCommand, RouterCommand, ImportPasswordsEvent>(
    router
  ) { command ->
    when (command) {
      GoToMainListScreen -> switchToNewRoot(Screens.MainListScreen)
      GoBack -> goBack()
    }
  }
}
