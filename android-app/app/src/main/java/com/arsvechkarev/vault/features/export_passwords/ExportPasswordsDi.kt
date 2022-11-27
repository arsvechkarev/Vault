package com.arsvechkarev.vault.features.export_passwords

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.export_passwords.actors.ExportPasswordsActor
import com.arsvechkarev.vault.features.export_passwords.actors.GetFilenameFromUriActor
import com.arsvechkarev.vault.features.export_passwords.actors.GetPasswordsFileUriActor

fun ExportPasswordsStore(
  coreComponent: CoreComponent
): TeaStore<ExportPasswordsState, ExportPasswordsUiEvent, ExportPasswordsNews> {
  return TeaStoreImpl(
    actors = listOf(
      GetPasswordsFileUriActor(coreComponent.fileSaver),
      GetFilenameFromUriActor(coreComponent.filenameFromUriRetriever),
      ExportPasswordsActor(
        coreComponent.application,
        coreComponent.fileSaver,
        coreComponent.dispatchersFacade,
      ),
    ),
    reducer = ExportPasswordsReducer(coreComponent.router),
    initialState = ExportPasswordsState()
  )
}

