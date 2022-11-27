package com.arsvechkarev.vault.features.creating_master_password.actors

import buisnesslogic.MasterPasswordChecker
import buisnesslogic.MasterPasswordHolder
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.domain.AuthChecker
import com.arsvechkarev.vault.features.creating_master_password.CMPCommands
import com.arsvechkarev.vault.features.creating_master_password.CMPEvents
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommand.FinishAuth
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.FinishedAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

class FinishAuthActor(
  private val authChecker: AuthChecker,
  private val masterPasswordChecker: MasterPasswordChecker,
  private val dispatchersFacade: DispatchersFacade,
) : Actor<CMPCommands, CMPEvents> {
  
  override fun handle(commands: Flow<CMPCommands>): Flow<CMPEvents> {
    return commands.filterIsInstance<FinishAuth>()
        .mapLatest { command ->
          withContext(dispatchersFacade.IO) {
            authChecker.setUserIsLoggedIn()
            masterPasswordChecker.initializeEncryptedFile(command.password)
            MasterPasswordHolder.setMasterPassword(command.password)
          }
          return@mapLatest FinishedAuth
        }
  }
}
