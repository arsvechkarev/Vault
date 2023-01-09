package com.arsvechkarev.vault.features.main_list

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.main_list.actors.ListeningEntriesChangesActor
import com.arsvechkarev.vault.features.main_list.actors.LoadMainDataActor
import com.arsvechkarev.vault.features.main_list.actors.MainListRouterActor
import com.arsvechkarev.vault.features.main_list.domain.EntriesMapper

fun MainListStore(
  coreComponent: CoreComponent
): TeaStore<MainListState, MainListUiEvent, Nothing> {
  val entriesMapper = EntriesMapper()
  return TeaStoreImpl(
    actors = listOf(
      LoadMainDataActor(
        coreComponent.listenableCachedEntriesStorage,
        coreComponent.masterPasswordProvider,
        entriesMapper
      ),
      ListeningEntriesChangesActor(coreComponent.listenableCachedEntriesStorage, entriesMapper),
      MainListRouterActor(coreComponent.router),
    ),
    reducer = MainListReducer(),
    initialState = MainListState()
  )
}
