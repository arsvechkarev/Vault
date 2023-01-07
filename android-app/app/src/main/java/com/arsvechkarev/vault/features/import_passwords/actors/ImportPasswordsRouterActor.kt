package com.arsvechkarev.vault.features.import_passwords.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.navigation.RouterActor
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsCommand
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsCommand.GoBack
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsEvent

fun ImportPasswordsRouterActor(
  router: Router
): Actor<ImportPasswordsCommand, ImportPasswordsEvent> {
  return RouterActor<ImportPasswordsCommand, GoBack, ImportPasswordsEvent>(router) {
    goBack()
  }
}
