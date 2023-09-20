package com.arsvechkarev.vault.features.import_passwords

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.import_passwords.actors.ImportPasswordsActor
import com.arsvechkarev.vault.features.import_passwords.actors.ImportPasswordsRouterActor

fun ImportPasswordsStore(
  coreComponent: CoreComponent
): TeaStore<ImportPasswordsState, ImportPasswordsUiEvent, Nothing> {
  return TeaStoreImpl(
    actors = listOf(
      ImportPasswordsActor(
        coreComponent.externalFileReader,
        coreComponent.databaseFileSaver,
        coreComponent.observableCachedDatabaseStorage,
      ),
      ImportPasswordsRouterActor(coreComponent.router),
    ),
    reducer = ImportPasswordsReducer(),
    initialState = ImportPasswordsState()
  )
}

