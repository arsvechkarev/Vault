package com.arsvechkarev.vault.features.main_list.domain

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
  ): List<DifferentiableItem> {
    val showUsernames = showUsernamesInteractor.getShowUsernames()
    val database = entriesStorage.getDatabase(masterPassword)
    return entriesListUiMapper.mapItems(database, showUsernames, filterQuery)
  }
}
