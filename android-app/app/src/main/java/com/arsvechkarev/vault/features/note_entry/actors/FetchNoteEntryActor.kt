package com.arsvechkarev.vault.features.note_entry.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.database.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.FetchNoteEntry
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent.MasterPasswordNull
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent.ReceivedNoteEntry
import domain.interactors.KeePassNoteModelInteractor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class FetchNoteEntryActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val storage: ObservableCachedDatabaseStorage,
  private val noteModelInteractor: KeePassNoteModelInteractor,
) : Actor<NoteEntryCommand, NoteEntryEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<NoteEntryCommand>): Flow<NoteEntryEvent> {
    return commands.filterIsInstance<FetchNoteEntry>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPasswordIfSet()
              ?: return@mapLatest MasterPasswordNull
          val database = storage.getDatabase(masterPassword)
          val noteEntry = noteModelInteractor.getNoteEntry(database, command.noteId)
          ReceivedNoteEntry(noteEntry)
        }
  }
}