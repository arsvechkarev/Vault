package com.arsvechkarev.vault.features.change_master_password.actors

import app.keemobile.kotpass.database.Credentials
import app.keemobile.kotpass.database.modifiers.modifyCredentials
import buisnesslogic.MasterPasswordHolder
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordCommand
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordCommand.ChangeMasterPassword
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordEvent
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordEvent.NewMasterPasswordSaved
import com.arsvechkarev.vault.features.common.data.storage.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class ChangeMasterPasswordActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val storage: ObservableCachedDatabaseStorage,
) : Actor<ChangeMasterPasswordCommand, ChangeMasterPasswordEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<ChangeMasterPasswordCommand>): Flow<ChangeMasterPasswordEvent> {
    return commands.filterIsInstance<ChangeMasterPassword>()
        .mapLatest { command ->
          val newMasterPassword = command.password
          val currentMasterPassword = masterPasswordProvider.provideMasterPassword()
          require(currentMasterPassword != newMasterPassword)
          val currentDatabase = storage.getDatabase(currentMasterPassword)
          val newDatabase = currentDatabase
              .modifyCredentials { Credentials.from(newMasterPassword.encryptedValueFiled) }
          storage.saveDatabase(newDatabase)
          MasterPasswordHolder.setMasterPassword(newMasterPassword)
          NewMasterPasswordSaved
        }
  }
}
