package com.arsvechkarev.vault.features.import_passwords

import android.net.Uri
import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.import_passwords.actors.ImportPasswordsActor
import com.arsvechkarev.vault.features.import_passwords.actors.ImportPasswordsRouterActor

fun ImportPasswordsStore(
  coreComponent: CoreComponent,
  selectedFileUri: Uri?,
  askForConfirmation: Boolean,
): TeaStore<ImportPasswordsState, ImportPasswordsUiEvent, Nothing> {
  return TeaStoreImpl(
    actors = listOf(
      ImportPasswordsActor(
        coreComponent.externalFileReader,
        coreComponent.observableCachedDatabaseStorage,
      ),
      ImportPasswordsRouterActor(coreComponent.router),
    ),
    reducer = ImportPasswordsReducer(),
    initialState = ImportPasswordsState(selectedFileUri, askForConfirmation)
  )
}

