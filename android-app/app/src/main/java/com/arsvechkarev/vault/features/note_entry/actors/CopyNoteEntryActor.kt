package com.arsvechkarev.vault.features.note_entry.actors

import com.arsvechkarev.vault.core.extensions.emptyMap
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.domain.Clipboard
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.Copy
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

class CopyNoteEntryActor(
  private val clipboard: Clipboard,
) : Actor<NoteEntryCommand, NoteEntryEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<NoteEntryCommand>): Flow<NoteEntryEvent> {
    return commands.filterIsInstance<Copy>()
        .emptyMap { clipboard.copyToClipboard(it.labelRes, it.text) }
  }
}
