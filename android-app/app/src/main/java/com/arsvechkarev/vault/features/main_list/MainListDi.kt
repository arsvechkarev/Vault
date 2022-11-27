package com.arsvechkarev.vault.features.main_list

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.main_list.actors.ListeningServicesChangesActor
import com.arsvechkarev.vault.features.main_list.actors.LoadMainDataActor
import com.arsvechkarev.vault.features.main_list.actors.MainListRouterActor

fun MainListStore(
  coreComponent: CoreComponent
): TeaStore<MainListState, MainListUiEvent, Nothing> {
  return TeaStoreImpl(
    actors = listOf(
      LoadMainDataActor(
        coreComponent.listenableCachedPasswordsStorage,
        coreComponent.masterPasswordProvider
      ),
      ListeningServicesChangesActor(coreComponent.listenableCachedPasswordsStorage),
      MainListRouterActor(coreComponent.router),
    ),
    reducer = MainListReducer(),
    initialState = MainListState()
  )
}
