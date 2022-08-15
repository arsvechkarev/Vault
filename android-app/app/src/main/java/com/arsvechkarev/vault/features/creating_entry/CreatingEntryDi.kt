package com.arsvechkarev.vault.features.creating_entry

import com.arsvechkarev.vault.core.di.AppComponent
import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.creating_entry.actors.ValidateInputActor

fun CreatingEntryStore(
  appComponent: AppComponent
): TeaStore<CreatingEntryState, CreatingEntryUiEvent, Nothing> {
  return TeaStoreImpl(
    actors = listOf(ValidateInputActor()),
    reducer = CreatingEntryReducer(appComponent.router),
    initialState = CreatingEntryState()
  )
}
