package com.arsvechkarev.vault.features.import_passwords

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.import_passwords.actors.ImportPasswordsActor

fun ImportPasswordsStore(
  coreComponent: CoreComponent
): TeaStore<ImportPasswordsState, ImportPasswordsUiEvent, Nothing> {
  return TeaStoreImpl(
    actors = listOf(
      ImportPasswordsActor(
        coreComponent.fileReader,
        coreComponent.cryptography,
        coreComponent.fileSaver,
        coreComponent.listenableCachedPasswordsStorage,
      )
    ),
    reducer = ImportPasswordsReducer(coreComponent.router),
    initialState = ImportPasswordsState()
  )
}

