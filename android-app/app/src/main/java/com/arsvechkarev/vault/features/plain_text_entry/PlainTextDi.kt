package com.arsvechkarev.vault.features.plain_text_entry

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.common.model.PlainTextItem
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextState.ExistingEntry
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextState.NewEntry
import com.arsvechkarev.vault.features.plain_text_entry.actors.CopyPlainTextActor
import com.arsvechkarev.vault.features.plain_text_entry.actors.DeletePlainTextActor
import com.arsvechkarev.vault.features.plain_text_entry.actors.PlainTextRouterActor
import com.arsvechkarev.vault.features.plain_text_entry.actors.SavePlainTextActor
import com.arsvechkarev.vault.features.plain_text_entry.actors.UpdatePlainTextActor

fun PlainTextStore(
  coreComponent: CoreComponent,
  plainTextItem: PlainTextItem?
): TeaStore<PlainTextState, PlainTextUiEvent, PlainTextNews> {
  return TeaStoreImpl(
    actors = listOf(
      SavePlainTextActor(
        coreComponent.masterPasswordProvider,
        coreComponent.listenableCachedEntriesStorage
      ),
      UpdatePlainTextActor(
        coreComponent.masterPasswordProvider,
        coreComponent.listenableCachedEntriesStorage
      ),
      DeletePlainTextActor(
        coreComponent.masterPasswordProvider,
        coreComponent.listenableCachedEntriesStorage
      ),
      CopyPlainTextActor(coreComponent.clipboard),
      PlainTextRouterActor(coreComponent.router),
    ),
    reducer = PlainTextReducer(),
    initialState = if (plainTextItem != null) ExistingEntry(plainTextItem) else NewEntry()
  )
}
