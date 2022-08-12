package com.arsvechkarev.vault.features.main_list.actors

import com.arsvechkarev.vault.core.ListenableCachedPasswordStorage
import com.arsvechkarev.vault.core.MasterPasswordProvider
import com.arsvechkarev.vault.core.State
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.main_list.MainListCommand
import com.arsvechkarev.vault.features.main_list.MainListCommand.LoadData
import com.arsvechkarev.vault.features.main_list.MainListEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class LoadMainDataActor(
  private val passwordStorage: ListenableCachedPasswordStorage,
  private val masterPasswordProvider: MasterPasswordProvider,
) : Actor<MainListCommand, MainListEvent> {
  
  override fun handle(commands: Flow<MainListCommand>): Flow<MainListEvent> {
    return commands
        .filterIsInstance<LoadData>()
        .mapLatest {
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          val passwords = passwordStorage.getPasswords(masterPassword)
          val state = if (passwords.isNotEmpty()) State.success(passwords) else State.empty()
          MainListEvent.UpdateData(state)
        }
  }
}