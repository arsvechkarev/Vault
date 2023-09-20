package com.arsvechkarev.vault.features.plain_text_info

import buisnesslogic.model.PlainTextEntry
import buisnesslogic.model.PlainTextEntryData
import com.arsvechkarev.vault.features.common.TextState

sealed interface PlainTextEvent {
  class ReceivedPlainTextEntry(val plainTextEntry: PlainTextEntry) : PlainTextEvent
  class NotifyEntryCreated(val plainTextEntry: PlainTextEntry) : PlainTextEvent
  object NotifyEntryDeleted : PlainTextEvent
  
  sealed interface UpdatedPlainText : PlainTextEvent {
    class UpdatedTitle(val plainTextEntry: PlainTextEntry) : UpdatedPlainText
    class UpdatedText(val plainTextEntry: PlainTextEntry) : UpdatedPlainText
  }
}

sealed interface PlainTextUiEvent : PlainTextEvent {
  object OnInit : PlainTextUiEvent
  class OnTitleChanged(val title: String) : PlainTextUiEvent
  class OnTextChanged(val text: String) : PlainTextUiEvent
  object OnBackPressed : PlainTextUiEvent
  
  object OnSaveClicked : PlainTextUiEvent
  object OnDeleteClicked : PlainTextUiEvent
  object OnConfirmedSaving : PlainTextUiEvent
  object OnConfirmedDeleting : PlainTextUiEvent
  object OnDialogHidden : PlainTextUiEvent
  
  object OnTitleActionClicked : PlainTextUiEvent
  object OnTextActionClicked : PlainTextUiEvent
}

sealed interface PlainTextCommand {
  
  class FetchPlainTextEntry(val plainTextId: String) : PlainTextCommand
  
  class SavePlainText(val data: PlainTextEntryData) : PlainTextCommand
  class DeletePlainText(val plainTextId: String) : PlainTextCommand
  
  class Copy(val labelRes: Int, val text: String) : PlainTextCommand
  
  object GoBack : PlainTextCommand
  
  sealed class UpdateItem(val plainTextEntry: PlainTextEntry) : PlainTextCommand {
    class UpdateText(item: PlainTextEntry) : UpdateItem(item)
    class UpdateTitle(item: PlainTextEntry) : UpdateItem(item)
  }
}

sealed interface PlainTextNews {
  object ShowEntryCreated : PlainTextNews
  object ShowTitleCopied : PlainTextNews
  object ShowTextCopied : PlainTextNews
  class SetTitle(val title: String) : PlainTextNews
  class SetText(val text: String) : PlainTextNews
}

sealed interface PlainTextState {
  
  data class NewEntry(
    val title: String = "",
    val text: String = "",
    val showTitleIsEmpty: Boolean = false,
    val showConfirmSaveDialog: Boolean = false,
  ) : PlainTextState
  
  data class ExistingEntry(
    val plainTextId: String,
    val plainTextEntry: PlainTextEntry? = null,
    val titleState: TextState = TextState.empty(),
    val textState: TextState = TextState.empty(),
    val showConfirmDeleteDialog: Boolean = false,
    val showLoadingDialog: Boolean = false,
  ) : PlainTextState {
    
    val plainTextEntryNonNull: PlainTextEntry get() = requireNotNull(plainTextEntry)
    
    val isEditingSomething get() = titleState.isEditingNow || textState.isEditingNow
  }
}
