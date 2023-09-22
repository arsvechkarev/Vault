package com.arsvechkarev.vault.features.creating_master_password.actors

import buisnesslogic.MasterPasswordChecker
import buisnesslogic.MasterPasswordHolder
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_master_password.CMPCommands
import com.arsvechkarev.vault.features.creating_master_password.CMPEvents
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordCommand.FinishAuth
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordEvent.FinishedAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class FinishAuthActor(
  private val masterPasswordChecker: MasterPasswordChecker,
) : Actor<CMPCommands, CMPEvents> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<CMPCommands>): Flow<CMPEvents> {
    return commands.filterIsInstance<FinishAuth>()
        .mapLatest { command ->
          masterPasswordChecker.initializeEncryptedFile(command.password)
          MasterPasswordHolder.setMasterPassword(command.password)
          return@mapLatest FinishedAuth
        }
  }
}
