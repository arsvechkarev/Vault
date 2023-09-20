package com.arsvechkarev.vault.features.main_list

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.main_list.actors.ListeningEntriesChangesActor
import com.arsvechkarev.vault.features.main_list.actors.LoadMainDataActor
import com.arsvechkarev.vault.features.main_list.actors.MainListRouterActor
import com.arsvechkarev.vault.features.main_list.domain.EntriesListUiMapper

fun MainListStore(
  coreComponent: CoreComponent
): TeaStore<MainListState, MainListUiEvent, Nothing> {
  val entriesListUiMapper = EntriesListUiMapper()
  return TeaStoreImpl(
    actors = listOf(
      LoadMainDataActor(
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.masterPasswordProvider,
        entriesListUiMapper
      ),
      ListeningEntriesChangesActor(coreComponent.observableCachedDatabaseStorage,
        entriesListUiMapper),
      MainListRouterActor(coreComponent.router),
    ),
    reducer = MainListReducer(),
    initialState = MainListState()
  )
}
