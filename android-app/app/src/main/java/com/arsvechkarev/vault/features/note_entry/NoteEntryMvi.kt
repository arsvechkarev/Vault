package com.arsvechkarev.vault.features.note_entry

import com.arsvechkarev.vault.features.common.TextState
import domain.model.NoteEntry
import domain.model.NoteEntryData

sealed interface NoteEntryEvent {
  class ReceivedNoteEntry(val noteEntry: NoteEntry) : NoteEntryEvent
  class NotifyEntryCreated(val noteEntry: NoteEntry) : NoteEntryEvent
  object NotifyEntryDeleted : NoteEntryEvent
  object MasterPasswordNull : NoteEntryEvent
  
  sealed interface UpdatedNoteEntry : NoteEntryEvent {
    class UpdatedTitle(val noteEntry: NoteEntry) : UpdatedNoteEntry
    class UpdatedText(val noteEntry: NoteEntry) : UpdatedNoteEntry
    class UpdatedIsFavorite(val noteEntry: NoteEntry) : UpdatedNoteEntry
  }
}

sealed interface NoteEntryUiEvent : NoteEntryEvent {
  object OnInit : NoteEntryUiEvent
  class OnTitleChanged(val title: String) : NoteEntryUiEvent
  class OnTextChanged(val text: String) : NoteEntryUiEvent
  object OnBackPressed : NoteEntryUiEvent
  
  object OnSaveClicked : NoteEntryUiEvent
  object OnFavoriteClicked : NoteEntryUiEvent
  object OnDeleteClicked : NoteEntryUiEvent
  object OnConfirmedSaving : NoteEntryUiEvent
  object OnConfirmedDeleting : NoteEntryUiEvent
  object OnDialogHidden : NoteEntryUiEvent
  
  object OnTitleActionClicked : NoteEntryUiEvent
  object OnTextActionClicked : NoteEntryUiEvent
}

sealed interface NoteEntryCommand {
  
  class FetchNoteEntry(val noteId: String) : NoteEntryCommand
  
  class SaveNoteEntry(val data: NoteEntryData) : NoteEntryCommand
  class DeleteNoteEntry(val noteId: String) : NoteEntryCommand
  
  class Copy(val labelRes: Int, val text: String) : NoteEntryCommand
  
  sealed class UpdateNoteEntry(val noteEntry: NoteEntry) : NoteEntryCommand {
    class UpdateText(item: NoteEntry) : UpdateNoteEntry(item)
    class UpdateTitle(item: NoteEntry) : UpdateNoteEntry(item)
    class UpdateIsFavorite(item: NoteEntry) : UpdateNoteEntry(item)
  }
  
  sealed interface RouterCommand : NoteEntryCommand {
    object GoBack : RouterCommand
    object SwitchBackToLogin : RouterCommand
  }
}

sealed interface NoteEntryNews {
  object ShowNoteEntryCreated : NoteEntryNews
  object ShowTitleCopied : NoteEntryNews
  object ShowTextCopied : NoteEntryNews
}

sealed interface NoteEntryState {
  
  data class NewEntry(
    val title: String = "",
    val text: String = "",
    val showTitleIsEmptyError: Boolean = false
  ) : NoteEntryState
  
  data class ExistingEntry(
    val noteId: String,
    val noteEntry: NoteEntry? = null,
    val titleState: TextState = TextState.empty(),
    val textState: TextState = TextState.empty(),
    val showTitleIsEmptyError: Boolean = false,
    val showConfirmDeleteDialog: Boolean = false,
    val showLoadingDialog: Boolean = false,
  ) : NoteEntryState {
    
    val noteEntryNonNull: NoteEntry get() = requireNotNull(noteEntry)
    
    val isEditingSomething get() = titleState.isEditingNow || textState.isEditingNow
  }
}
