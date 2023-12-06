package com.arsvechkarev.vault.features.note_entry.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.database.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.UpdateNoteEntry
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.UpdateNoteEntry.UpdateIsFavorite
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.UpdateNoteEntry.UpdateText
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.UpdateNoteEntry.UpdateTitle
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent.UpdatedNoteEntry.UpdatedIsFavorite
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent.UpdatedNoteEntry.UpdatedText
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent.UpdatedNoteEntry.UpdatedTitle
import domain.interactors.KeePassNoteEntryInteractor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class UpdateNoteEntryActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val storage: ObservableCachedDatabaseStorage,
  private val noteEntryInteractor: KeePassNoteEntryInteractor
) : Actor<NoteEntryCommand, NoteEntryEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<NoteEntryCommand>): Flow<NoteEntryEvent> {
    return commands.filterIsInstance<UpdateNoteEntry>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          val database = storage.getDatabase(masterPassword)
          val newDatabase = noteEntryInteractor.editNote(database, command.noteEntry)
          storage.saveDatabase(newDatabase)
          when (command) {
            is UpdateTitle -> UpdatedTitle(command.noteEntry)
            is UpdateText -> UpdatedText(command.noteEntry)
            is UpdateIsFavorite -> UpdatedIsFavorite(command.noteEntry)
          }
        }
  }
}
