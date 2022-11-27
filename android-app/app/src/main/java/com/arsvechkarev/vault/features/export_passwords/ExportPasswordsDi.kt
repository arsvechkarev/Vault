package com.arsvechkarev.vault.features.export_passwords

import com.arsvechkarev.vault.core.di.AppComponent
import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.export_passwords.actors.GetFilenameFromUriActor
import com.arsvechkarev.vault.features.export_passwords.actors.GetPasswordsFileUriActor

fun ExportPasswordsStore(
  appComponent: AppComponent
): TeaStore<ExportPasswordsState, ExportPasswordsUiEvent, ExportPasswordsNews> {
  return TeaStoreImpl(
    actors = listOf(
      GetPasswordsFileUriActor(appComponent.fileSaver),
      GetFilenameFromUriActor(appComponent.filenameFromUriRetriever),
    ),
    reducer = ExportPasswordsReducer(appComponent.router),
    initialState = ExportPasswordsState()
  )
}

