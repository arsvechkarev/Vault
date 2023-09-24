package com.arsvechkarev.vault.features.settings.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens.MasterPasswordScreen
import com.arsvechkarev.vault.features.common.navigation.RouterActor
import com.arsvechkarev.vault.features.master_password.MasterPasswordScreenMode.CHANGE_EXISTING
import com.arsvechkarev.vault.features.settings.SettingsCommand
import com.arsvechkarev.vault.features.settings.SettingsCommand.RouterCommand
import com.arsvechkarev.vault.features.settings.SettingsCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.settings.SettingsEvent

fun SettingsRouterActor(router: Router): Actor<SettingsCommand, SettingsEvent> {
  return RouterActor<SettingsCommand, RouterCommand, SettingsEvent>(router) { command ->
    when (command) {
      is RouterCommand.GoToMasterPasswordScreen -> {
        goForward(MasterPasswordScreen(CHANGE_EXISTING))
      }
      GoBack -> goBack()
    }
  }
}