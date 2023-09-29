package com.arsvechkarev.vault.features.main_list

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.main_list.actors.ExportPasswordsActor
import com.arsvechkarev.vault.features.main_list.actors.ListenShowUsernamesActor
import com.arsvechkarev.vault.features.main_list.actors.ListeningEntriesChangesActor
import com.arsvechkarev.vault.features.main_list.actors.LoadMainDataActor
import com.arsvechkarev.vault.features.main_list.actors.MainListRouterActor

fun MainListStore(
  coreComponent: CoreComponent
): TeaStore<MainListState, MainListUiEvent, MainListNews> {
  return TeaStoreImpl(
    actors = listOf(
      LoadMainDataActor(
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.masterPasswordProvider,
        coreComponent.entriesListUiMapper,
        coreComponent.showUsernamesInteractor
      ),
      ListeningEntriesChangesActor(
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.entriesListUiMapper,
        coreComponent.showUsernamesInteractor
      ),
      ExportPasswordsActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.passwordsFileExporter
      ),
      ListenShowUsernamesActor(coreComponent.showUsernamesInteractor),
      MainListRouterActor(coreComponent.router),
    ),
    reducer = MainListReducer(),
    initialState = MainListState()
  )
}
