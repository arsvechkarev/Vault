package com.arsvechkarev.vault.features.plain_text_entry

import com.arsvechkarev.vault.features.common.TextState
import com.arsvechkarev.vault.features.common.model.PlainTextItem

sealed interface PlainTextEvent {
  class NotifyEntryCreated(val plainTextItem: PlainTextItem) : PlainTextEvent
  object NotifyEntryDeleted : PlainTextEvent
  
  sealed interface UpdatedPlainText : PlainTextEvent {
    class UpdatedTitle(val plainTextItem: PlainTextItem) : UpdatedPlainText
    class UpdatedText(val plainTextItem: PlainTextItem) : UpdatedPlainText
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
  
  class SavePlainText(val title: String, val text: String) : PlainTextCommand
  class DeletePlainText(val plainTextItem: PlainTextItem) : PlainTextCommand
  
  class Copy(val labelRes: Int, val text: String) : PlainTextCommand
  
  object GoBack : PlainTextCommand
  
  sealed class UpdateItem(val plainTextItem: PlainTextItem) : PlainTextCommand {
    class UpdateText(item: PlainTextItem) : UpdateItem(item)
    class UpdateTitle(item: PlainTextItem) : UpdateItem(item)
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
    val plainTextItem: PlainTextItem,
    val titleState: TextState = TextState(plainTextItem.title),
    val textState: TextState = TextState(plainTextItem.text),
    val showConfirmDeleteDialog: Boolean = false,
    val showLoadingDialog: Boolean = false,
  ) : PlainTextState {
    
    val isEditingSomething get() = titleState.isEditingNow || textState.isEditingNow
  }
}
