package com.arsvechkarev.vault.features.main_list

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.main_list.actors.ExportPasswordsActor
import com.arsvechkarev.vault.features.main_list.actors.ListenEntriesChangesActor
import com.arsvechkarev.vault.features.main_list.actors.ListenNetworkAvailabilityActor
import com.arsvechkarev.vault.features.main_list.actors.ListenReloadPasswordImagesActor
import com.arsvechkarev.vault.features.main_list.actors.ListenShowUsernamesActor
import com.arsvechkarev.vault.features.main_list.actors.LoadMainDataActor
import com.arsvechkarev.vault.features.main_list.actors.MainListRouterActor
import com.arsvechkarev.vault.features.main_list.actors.SearchEntriesActor

fun MainListStore(
  coreComponent: CoreComponent
): TeaStore<MainListState, MainListUiEvent, MainListNews> {
  return TeaStoreImpl(
    actors = listOf(
      LoadMainDataActor(
        coreComponent.masterPasswordProvider,
        coreComponent.loadEntriesInteractor
      ),
      SearchEntriesActor(
        coreComponent.masterPasswordProvider,
        coreComponent.loadEntriesInteractor
      ),
      ListenEntriesChangesActor(
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
      ListenReloadPasswordImagesActor(coreComponent.reloadImagesObserver),
      ListenNetworkAvailabilityActor(coreComponent.networkAvailabilityProvider),
      MainListRouterActor(coreComponent.router),
    ),
    reducer = MainListReducer(),
    initialState = MainListState()
  )
}
