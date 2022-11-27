package com.arsvechkarev.vault.features.import_passwords

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.AppComponent
import com.arsvechkarev.vault.features.import_passwords.actors.ImportPasswordsActor

fun ImportPasswordsStore(
  appComponent: AppComponent
): TeaStore<ImportPasswordsState, ImportPasswordsUiEvent, Nothing> {
  return TeaStoreImpl(
    actors = listOf(
      ImportPasswordsActor(
        appComponent.fileReader,
        appComponent.cryptography,
        appComponent.fileSaver,
        appComponent.listenableCachedPasswordsStorage,
      )
    ),
    reducer = ImportPasswordsReducer(appComponent.router),
    initialState = ImportPasswordsState()
  )
}

