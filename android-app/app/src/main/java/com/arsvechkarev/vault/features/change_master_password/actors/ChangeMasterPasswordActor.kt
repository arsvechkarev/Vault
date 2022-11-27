package com.arsvechkarev.vault.features.change_master_password.actors

import buisnesslogic.MasterPasswordChecker
import buisnesslogic.MasterPasswordHolder
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordCommand
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordCommand.ChangeMasterPassword
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordEvent
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordEvent.NewMasterPasswordSaved
import com.arsvechkarev.vault.features.common.data.storage.ListenableCachedPasswordsStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class ChangeMasterPasswordActor(
  private val masterPasswordChecker: MasterPasswordChecker,
  private val masterPasswordProvider: MasterPasswordProvider,
  private val storage: ListenableCachedPasswordsStorage,
) : Actor<ChangeMasterPasswordCommand, ChangeMasterPasswordEvent> {
  
  override fun handle(commands: Flow<ChangeMasterPasswordCommand>): Flow<ChangeMasterPasswordEvent> {
    return commands.filterIsInstance<ChangeMasterPassword>()
        .mapLatest { command ->
          val newMasterPassword = command.password
          val currentMasterPassword = masterPasswordProvider.provideMasterPassword()
          require(currentMasterPassword != newMasterPassword)
          val allPasswords = storage.getPasswords(currentMasterPassword)
          masterPasswordChecker.initializeEncryptedFile(newMasterPassword)
          storage.savePasswords(newMasterPassword, allPasswords)
          MasterPasswordHolder.setMasterPassword(newMasterPassword)
          NewMasterPasswordSaved
        }
  }
}
