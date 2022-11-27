package com.arsvechkarev.vault.features.creating_master_password.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens
import com.arsvechkarev.vault.features.common.presentation.RouterActor
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommand
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommand.RouterCommand
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommand.RouterCommand.GoToMainListScreen
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent

fun CreatingMasterPasswordRouterActor(
  router: Router
): Actor<CreatingMasterPasswordCommand, CreatingMasterPasswordEvent> {
  return RouterActor<CreatingMasterPasswordCommand, RouterCommand,
      CreatingMasterPasswordEvent>(router) { command ->
    when (command) {
      GoBack -> {
        goBack()
      }
      GoToMainListScreen -> {
        switchToNewRoot(Screens.MainListScreen)
      }
    }
  }
}
