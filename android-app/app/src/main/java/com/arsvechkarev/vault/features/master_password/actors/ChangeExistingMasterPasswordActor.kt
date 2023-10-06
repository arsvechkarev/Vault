package com.arsvechkarev.vault.features.master_password.actors

import app.keemobile.kotpass.database.Credentials
import app.keemobile.kotpass.database.modifiers.modifyCredentials
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.features.common.data.database.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.domain.GlobalChangeMasterPasswordPublisher
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.master_password.MasterPasswordCommand
import com.arsvechkarev.vault.features.master_password.MasterPasswordCommand.ChangeExistingMasterPassword
import com.arsvechkarev.vault.features.master_password.MasterPasswordEvent
import com.arsvechkarev.vault.features.master_password.MasterPasswordEvent.FinishedMasterPasswordSaving
import domain.MasterPasswordHolder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class ChangeExistingMasterPasswordActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val storage: ObservableCachedDatabaseStorage,
  private val globalChangeMasterPasswordPublisher: GlobalChangeMasterPasswordPublisher,
) : Actor<MasterPasswordCommand, MasterPasswordEvent> {
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<MasterPasswordCommand>): Flow<MasterPasswordEvent> {
    return commands.filterIsInstance<ChangeExistingMasterPassword>()
        .mapLatest { command ->
          val newMasterPassword = command.password
          val currentMasterPassword = masterPasswordProvider.provideMasterPassword()
          require(currentMasterPassword != newMasterPassword)
          val currentDatabase = storage.getDatabase(currentMasterPassword)
          val newDatabase = currentDatabase
              .modifyCredentials { Credentials.from(newMasterPassword.encryptedValueFiled) }
          storage.saveDatabase(newDatabase)
          MasterPasswordHolder.setMasterPassword(newMasterPassword)
          delay(Durations.StubDelay)
          globalChangeMasterPasswordPublisher.publishMasterPasswordChanged()
          FinishedMasterPasswordSaving
        }
  }
}