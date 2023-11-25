package com.arsvechkarev.vault.features.note_entry.actors

import app.keemobile.kotpass.database.modifiers.removeEntry
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.database.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.DeleteNoteEntry
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent.NotifyEntryDeleted
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import java.util.UUID

class DeleteNoteEntryActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val storage: ObservableCachedDatabaseStorage,
) : Actor<NoteEntryCommand, NoteEntryEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<NoteEntryCommand>): Flow<NoteEntryEvent> {
    return commands.filterIsInstance<DeleteNoteEntry>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          val database = storage.getDatabase(masterPassword)
          val newDatabase = database.removeEntry(UUID.fromString(command.noteId))
          storage.saveDatabase(newDatabase)
          NotifyEntryDeleted
        }
  }
}
