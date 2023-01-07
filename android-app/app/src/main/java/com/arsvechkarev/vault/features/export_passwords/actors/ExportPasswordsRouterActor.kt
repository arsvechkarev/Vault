package com.arsvechkarev.vault.features.export_passwords.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.navigation.RouterActor
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsCommand
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsCommand.GoBack
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsEvent

fun ExportPasswordsRouterActor(
  router: Router
): Actor<ExportPasswordsCommand, ExportPasswordsEvent> {
  return RouterActor<ExportPasswordsCommand, GoBack, ExportPasswordsEvent>(router) {
    goBack()
  }
}
