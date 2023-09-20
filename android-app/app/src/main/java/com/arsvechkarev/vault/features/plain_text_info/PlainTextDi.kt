package com.arsvechkarev.vault.features.plain_text_info

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.plain_text_info.PlainTextState.ExistingEntry
import com.arsvechkarev.vault.features.plain_text_info.PlainTextState.NewEntry
import com.arsvechkarev.vault.features.plain_text_info.actors.CopyPlainTextActor
import com.arsvechkarev.vault.features.plain_text_info.actors.DeletePlainTextActor
import com.arsvechkarev.vault.features.plain_text_info.actors.PlainTextRouterActor
import com.arsvechkarev.vault.features.plain_text_info.actors.SavePlainTextActor
import com.arsvechkarev.vault.features.plain_text_info.actors.UpdatePlainTextActor

fun PlainTextStore(
  coreComponent: CoreComponent,
  plainTextId: String?
): TeaStore<PlainTextState, PlainTextUiEvent, PlainTextNews> {
  return TeaStoreImpl(
    actors = listOf(
      SavePlainTextActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.keePassPlainTextModelInteractor,
      ),
      UpdatePlainTextActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.keePassPlainTextModelInteractor,
      ),
      DeletePlainTextActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage
      ),
      CopyPlainTextActor(coreComponent.clipboard),
      PlainTextRouterActor(coreComponent.router),
    ),
    reducer = PlainTextReducer(),
    initialState = if (plainTextId != null) ExistingEntry(plainTextId) else NewEntry()
  )
}
