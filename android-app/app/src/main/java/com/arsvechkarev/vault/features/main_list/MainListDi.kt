package com.arsvechkarev.vault.features.main_list

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.AppComponent
import com.arsvechkarev.vault.features.main_list.actors.ListeningServicesChangesActor
import com.arsvechkarev.vault.features.main_list.actors.LoadMainDataActor

fun MainListStore(
  appComponent: AppComponent
): TeaStore<MainListState, MainListUiEvent, Nothing> {
  return TeaStoreImpl(
    actors = listOf(
      LoadMainDataActor(
        appComponent.listenableCachedPasswordsStorage,
        appComponent.masterPasswordProvider
      ),
      ListeningServicesChangesActor(appComponent.listenableCachedPasswordsStorage),
    ),
    reducer = MainListReducer(appComponent.router),
    initialState = MainListState()
  )
}
