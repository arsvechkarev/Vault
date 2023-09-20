package com.arsvechkarev.vault.features.main_list.actors

import com.arsvechkarev.vault.core.ScreenState
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.storage.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.main_list.MainListCommand
import com.arsvechkarev.vault.features.main_list.MainListEvent
import com.arsvechkarev.vault.features.main_list.MainListEvent.UpdateData
import com.arsvechkarev.vault.features.main_list.domain.EntriesListUiMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ListeningEntriesChangesActor(
  private val passwordStorage: ObservableCachedDatabaseStorage,
  private val entriesListUiMapper: EntriesListUiMapper
) : Actor<MainListCommand, MainListEvent> {
  
  override fun handle(commands: Flow<MainListCommand>): Flow<MainListEvent> {
    return passwordStorage.databaseFlow.map { entries ->
      val entriesItems = entriesListUiMapper.mapEntries(entries)
      val state = if (entriesItems.isNotEmpty()) {
        ScreenState.success(entriesItems)
      } else {
        ScreenState.empty()
      }
      UpdateData(state)
    }
  }
}
