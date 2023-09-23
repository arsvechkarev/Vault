package com.arsvechkarev.vault.features.master_password.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens
import com.arsvechkarev.vault.features.common.navigation.RouterActor
import com.arsvechkarev.vault.features.master_password.MasterPasswordCommand
import com.arsvechkarev.vault.features.master_password.MasterPasswordCommand.RouterCommand
import com.arsvechkarev.vault.features.master_password.MasterPasswordCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.master_password.MasterPasswordCommand.RouterCommand.GoToMainListScreen
import com.arsvechkarev.vault.features.master_password.MasterPasswordEvent

fun CreatingMasterPasswordRouterActor(
  router: Router
): Actor<MasterPasswordCommand, MasterPasswordEvent> {
  return RouterActor<MasterPasswordCommand, RouterCommand,
      MasterPasswordEvent>(router) { command ->
    when (command) {
      GoBack -> goBack()
      GoToMainListScreen -> switchToNewRoot(Screens.MainListScreen)
    }
  }
}
