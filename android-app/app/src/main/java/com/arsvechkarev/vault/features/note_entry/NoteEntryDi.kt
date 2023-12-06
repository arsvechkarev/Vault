package com.arsvechkarev.vault.features.note_entry

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.note_entry.NoteEntryState.ExistingEntry
import com.arsvechkarev.vault.features.note_entry.NoteEntryState.NewEntry
import com.arsvechkarev.vault.features.note_entry.actors.CopyNoteEntryActor
import com.arsvechkarev.vault.features.note_entry.actors.DeleteNoteEntryActor
import com.arsvechkarev.vault.features.note_entry.actors.FetchNoteEntryActor
import com.arsvechkarev.vault.features.note_entry.actors.NoteRouterActor
import com.arsvechkarev.vault.features.note_entry.actors.SaveNoteEntryActor
import com.arsvechkarev.vault.features.note_entry.actors.UpdateNoteEntryActor

fun NoteEntryStore(
  coreComponent: CoreComponent,
  noteId: String?
): TeaStore<NoteEntryState, NoteEntryUiEvent, NoteEntryNews> {
  return TeaStoreImpl(
    actors = listOf(
      FetchNoteEntryActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.keePassNoteEntryInteractor
      ),
      SaveNoteEntryActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.keePassNoteEntryInteractor,
      ),
      UpdateNoteEntryActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.keePassNoteEntryInteractor,
      ),
      DeleteNoteEntryActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage
      ),
      CopyNoteEntryActor(coreComponent.clipboard),
      NoteRouterActor(coreComponent.router),
    ),
    reducer = NoteEntryReducer(),
    initialState = if (noteId != null) ExistingEntry(noteId) else NewEntry()
  )
}
