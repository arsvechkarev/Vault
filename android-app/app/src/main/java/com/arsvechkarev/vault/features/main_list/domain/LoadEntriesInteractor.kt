package com.arsvechkarev.vault.features.main_list.domain

import com.arsvechkarev.vault.core.ScreenState
import com.arsvechkarev.vault.features.common.data.database.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.domain.ShowUsernamesInteractor
import com.arsvechkarev.vault.recycler.DifferentiableItem
import domain.Password

class LoadEntriesInteractor(
  private val entriesStorage: ObservableCachedDatabaseStorage,
  private val entriesListUiMapper: EntriesListUiMapper,
  private val showUsernamesInteractor: ShowUsernamesInteractor,
) {
  
  suspend fun loadEntries(
    masterPassword: Password,
    filterQuery: String
  ): ScreenState<List<DifferentiableItem>> {
    val showUsernames = showUsernamesInteractor.getShowUsernames()
    val database = entriesStorage.getDatabase(masterPassword)
    val entriesItems = entriesListUiMapper.mapItems(database, showUsernames, filterQuery)
    val state = if (entriesItems.isNotEmpty()) {
      ScreenState.success(entriesItems)
    } else {
      ScreenState.empty()
    }
    return state
  }
}
