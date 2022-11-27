package com.arsvechkarev.vault.features.export_passwords

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.AppComponent
import com.arsvechkarev.vault.features.export_passwords.actors.ExportPasswordsActor
import com.arsvechkarev.vault.features.export_passwords.actors.GetFilenameFromUriActor
import com.arsvechkarev.vault.features.export_passwords.actors.GetPasswordsFileUriActor

fun ExportPasswordsStore(
  appComponent: AppComponent
): TeaStore<ExportPasswordsState, ExportPasswordsUiEvent, ExportPasswordsNews> {
  return TeaStoreImpl(
    actors = listOf(
      GetPasswordsFileUriActor(appComponent.fileSaver),
      GetFilenameFromUriActor(appComponent.filenameFromUriRetriever),
      ExportPasswordsActor(
        appComponent.application,
        appComponent.fileSaver,
        appComponent.dispatchersFacade,
      ),
    ),
    reducer = ExportPasswordsReducer(appComponent.router),
    initialState = ExportPasswordsState()
  )
}

