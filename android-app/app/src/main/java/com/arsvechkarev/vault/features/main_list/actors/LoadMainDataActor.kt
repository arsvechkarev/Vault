package com.arsvechkarev.vault.features.main_list.actors

import com.arsvechkarev.vault.core.ListScreenState
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.main_list.MainListCommand
import com.arsvechkarev.vault.features.main_list.MainListCommand.LoadData
import com.arsvechkarev.vault.features.main_list.MainListEvent
import com.arsvechkarev.vault.features.main_list.MainListEvent.MasterPasswordNull
import com.arsvechkarev.vault.features.main_list.MainListEvent.UpdateData
import com.arsvechkarev.vault.features.main_list.domain.LoadEntriesInteractor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class LoadMainDataActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val loadEntriesInteractor: LoadEntriesInteractor,
) : Actor<MainListCommand, MainListEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<MainListCommand>): Flow<MainListEvent> {
    return commands.filterIsInstance<LoadData>()
        .mapLatest {
          val masterPassword = masterPasswordProvider.provideMasterPasswordIfSet()
              ?: return@mapLatest MasterPasswordNull
          val entries = loadEntriesInteractor.loadEntries(masterPassword, filterQuery = "")
          val state = if (entries.isNotEmpty()) {
            ListScreenState.success(entries)
          } else {
            ListScreenState.empty()
          }
          UpdateData(state)
        }
  }
}
