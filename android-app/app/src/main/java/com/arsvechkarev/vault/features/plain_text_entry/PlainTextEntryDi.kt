package com.arsvechkarev.vault.features.plain_text_entry

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryState.ExistingEntry
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryState.NewEntry
import com.arsvechkarev.vault.features.plain_text_entry.actors.CopyPlainTextEntryActor
import com.arsvechkarev.vault.features.plain_text_entry.actors.DeletePlainTextEntryActor
import com.arsvechkarev.vault.features.plain_text_entry.actors.FetchPlainTextEntryActor
import com.arsvechkarev.vault.features.plain_text_entry.actors.PlainTextRouterActor
import com.arsvechkarev.vault.features.plain_text_entry.actors.SavePlainTextEntryActor
import com.arsvechkarev.vault.features.plain_text_entry.actors.UpdatePlainTextEntryActor

fun PlainTextEntryStore(
  coreComponent: CoreComponent,
  plainTextId: String?
): TeaStore<PlainTextEntryState, PlainTextEntryUiEvent, PlainTextEntryNews> {
  return TeaStoreImpl(
    actors = listOf(
      FetchPlainTextEntryActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.keePassPlainTextModelInteractor
      ),
      SavePlainTextEntryActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.keePassPlainTextModelInteractor,
      ),
      UpdatePlainTextEntryActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.keePassPlainTextModelInteractor,
      ),
      DeletePlainTextEntryActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage
      ),
      CopyPlainTextEntryActor(coreComponent.clipboard),
      PlainTextRouterActor(coreComponent.router),
    ),
    reducer = PlainTextEntryReducer(),
    initialState = if (plainTextId != null) ExistingEntry(plainTextId) else NewEntry()
  )
}
