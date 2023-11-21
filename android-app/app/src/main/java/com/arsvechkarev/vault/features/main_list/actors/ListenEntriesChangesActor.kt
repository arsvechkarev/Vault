package com.arsvechkarev.vault.features.main_list.actors

import com.arsvechkarev.vault.core.ScreenState
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.database.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.domain.ShowUsernamesInteractor
import com.arsvechkarev.vault.features.main_list.MainListCommand
import com.arsvechkarev.vault.features.main_list.MainListEvent
import com.arsvechkarev.vault.features.main_list.MainListEvent.UpdateData
import com.arsvechkarev.vault.features.main_list.domain.EntriesListUiMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class ListenEntriesChangesActor(
  private val passwordStorage: ObservableCachedDatabaseStorage,
  private val entriesListUiMapper: EntriesListUiMapper,
  private val showUsernamesInteractor: ShowUsernamesInteractor,
) : Actor<MainListCommand, MainListEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<MainListCommand>): Flow<MainListEvent> {
    return passwordStorage.databaseFlow
        .mapLatest { database ->
          val showUsernames = showUsernamesInteractor.getShowUsernames()
          val entriesItems = entriesListUiMapper.mapItems(database, showUsernames, filterQuery = "")
          val state = if (entriesItems.isNotEmpty()) {
            ScreenState.success(entriesItems)
          } else {
            ScreenState.empty()
          }
          UpdateData(state)
        }
  }
}
