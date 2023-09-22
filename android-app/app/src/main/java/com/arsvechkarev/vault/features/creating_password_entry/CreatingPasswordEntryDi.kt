package com.arsvechkarev.vault.features.creating_password_entry

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.creating_password_entry.actors.CreatingEntryRouterActor
import com.arsvechkarev.vault.features.creating_password_entry.actors.SavingPasswordEntryActor

fun CreatingPasswordEntryStore(
  coreComponent: CoreComponent
): TeaStore<CreatingPasswordEntryState, CreatingPasswordEntryUiEvent, Nothing> {
  return TeaStoreImpl(
    actors = listOf(
      SavingPasswordEntryActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.keePassPasswordModelInteractor,
      ),
      CreatingEntryRouterActor(coreComponent.router)
    ),
    reducer = CreatingPasswordEntryReducer(),
    initialState = CreatingPasswordEntryState()
  )
}
