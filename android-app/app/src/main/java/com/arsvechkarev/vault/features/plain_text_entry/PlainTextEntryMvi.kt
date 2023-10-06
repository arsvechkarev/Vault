package com.arsvechkarev.vault.features.plain_text_entry

import com.arsvechkarev.vault.features.common.TextState
import domain.model.PlainTextEntry
import domain.model.PlainTextEntryData

sealed interface PlainTextEntryEvent {
  class ReceivedPlainTextEntry(val plainTextEntry: PlainTextEntry) : PlainTextEntryEvent
  class NotifyEntryCreated(val plainTextEntry: PlainTextEntry) : PlainTextEntryEvent
  object NotifyEntryDeleted : PlainTextEntryEvent
  
  sealed interface UpdatedPlainTextEntry : PlainTextEntryEvent {
    class UpdatedTitle(val plainTextEntry: PlainTextEntry) : UpdatedPlainTextEntry
    class UpdatedText(val plainTextEntry: PlainTextEntry) : UpdatedPlainTextEntry
    class UpdatedIsFavorite(val plainTextEntry: PlainTextEntry) : UpdatedPlainTextEntry
  }
}

sealed interface PlainTextEntryUiEvent : PlainTextEntryEvent {
  object OnInit : PlainTextEntryUiEvent
  class OnTitleChanged(val title: String) : PlainTextEntryUiEvent
  class OnTextChanged(val text: String) : PlainTextEntryUiEvent
  object OnBackPressed : PlainTextEntryUiEvent
  
  object OnSaveClicked : PlainTextEntryUiEvent
  object OnFavoriteClicked : PlainTextEntryUiEvent
  object OnDeleteClicked : PlainTextEntryUiEvent
  object OnConfirmedSaving : PlainTextEntryUiEvent
  object OnConfirmedDeleting : PlainTextEntryUiEvent
  object OnDialogHidden : PlainTextEntryUiEvent
  
  object OnTitleActionClicked : PlainTextEntryUiEvent
  object OnTextActionClicked : PlainTextEntryUiEvent
}

sealed interface PlainTextEntryCommand {
  
  class FetchPlainTextEntry(val plainTextId: String) : PlainTextEntryCommand
  
  class SavePlainTextEntry(val data: PlainTextEntryData) : PlainTextEntryCommand
  class DeletePlainTextEntry(val plainTextId: String) : PlainTextEntryCommand
  
  class Copy(val labelRes: Int, val text: String) : PlainTextEntryCommand
  
  object GoBack : PlainTextEntryCommand
  
  sealed class UpdatePlainTextEntry(val plainTextEntry: PlainTextEntry) : PlainTextEntryCommand {
    class UpdateText(item: PlainTextEntry) : UpdatePlainTextEntry(item)
    class UpdateTitle(item: PlainTextEntry) : UpdatePlainTextEntry(item)
    class UpdateIsFavorite(item: PlainTextEntry) : UpdatePlainTextEntry(item)
  }
}

sealed interface PlainTextEntryNews {
  object ShowPlainTextEntryCreated : PlainTextEntryNews
  object ShowTitleCopied : PlainTextEntryNews
  object ShowTextCopied : PlainTextEntryNews
  class SetTitle(val title: String) : PlainTextEntryNews
  class SetText(val text: String) : PlainTextEntryNews
}

sealed interface PlainTextEntryState {
  
  data class NewEntry(
    val title: String = "",
    val text: String = "",
    val showTitleIsEmptyError: Boolean = false
  ) : PlainTextEntryState
  
  data class ExistingEntry(
    val plainTextId: String,
    val plainTextEntry: PlainTextEntry? = null,
    val titleState: TextState = TextState.empty(),
    val textState: TextState = TextState.empty(),
    val showTitleIsEmptyError: Boolean = false,
    val showConfirmDeleteDialog: Boolean = false,
    val showLoadingDialog: Boolean = false,
  ) : PlainTextEntryState {
    
    val plainTextEntryNonNull: PlainTextEntry get() = requireNotNull(plainTextEntry)
    
    val isEditingSomething get() = titleState.isEditingNow || textState.isEditingNow
  }
}
