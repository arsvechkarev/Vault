package com.arsvechkarev.vault.features.change_master_password.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordCommand
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordCommand.GoBack
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordEvent
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.navigation.RouterActor

fun ChangeMasterPasswordRouterActor(
  router: Router
): Actor<ChangeMasterPasswordCommand, ChangeMasterPasswordEvent> {
  return RouterActor<ChangeMasterPasswordCommand, GoBack, ChangeMasterPasswordEvent>(router) {
    goBack()
  }
}
