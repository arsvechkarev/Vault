package com.arsvechkarev.vault.features.creating_password.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.presentation.RouterActor
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand.GoBack
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent

fun CreatingPasswordRouterActor(
  router: Router
): Actor<CreatingPasswordCommand, CreatingPasswordEvent> {
  return RouterActor<CreatingPasswordCommand, GoBack, CreatingPasswordEvent>(router) {
    goBack()
  }
}
