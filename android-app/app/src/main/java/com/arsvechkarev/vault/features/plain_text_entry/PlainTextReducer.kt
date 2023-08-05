package com.arsvechkarev.vault.features.plain_text_entry

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.common.edit
import com.arsvechkarev.vault.features.common.extensions.handleAction
import com.arsvechkarev.vault.features.common.reset
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextCommand.Copy
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextCommand.DeletePlainText
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextCommand.GoBack
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextCommand.SavePlainText
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextCommand.UpdateItem.UpdateText
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextCommand.UpdateItem.UpdateTitle
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEvent.NotifyEntryCreated
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEvent.NotifyEntryDeleted
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEvent.UpdatedPlainText.UpdatedText
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEvent.UpdatedPlainText.UpdatedTitle
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextNews.SetText
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextNews.SetTitle
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextNews.ShowEntryCreated
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextNews.ShowTextCopied
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextNews.ShowTitleCopied
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextState.ExistingEntry
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextState.NewEntry
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnConfirmedDeleting
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnConfirmedSaving
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnDeleteClicked
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnDialogHidden
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnInit
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnSaveClicked
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnTextActionClicked
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnTextChanged
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnTitleActionClicked
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnTitleChanged

class PlainTextReducer : DslReducer<PlainTextState, PlainTextEvent,
    PlainTextCommand, PlainTextNews>() {
  
  override fun dslReduce(event: PlainTextEvent) {
    val state = state
    when (event) {
      OnInit -> {
        sendSetInitialTextsNews()
      }
      
      is OnTitleChanged -> when (state) {
        is NewEntry -> {
          state { state.copy(showTitleIsEmpty = false, title = event.title) }
        }
        
        is ExistingEntry -> {
          state { state.copy(titleState = state.titleState.edit(event.title)) }
        }
      }
      
      is OnTextChanged -> when (state) {
        is NewEntry -> {
          state { state.copy(text = event.text) }
        }
        
        is ExistingEntry -> {
          state { state.copy(textState = state.textState.edit(event.text)) }
        }
      }
      
      OnSaveClicked -> {
        check(state is NewEntry)
        if (state.title.isBlank()) {
          state { state.copy(showTitleIsEmpty = true) }
        } else {
          state { state.copy(showConfirmSaveDialog = true) }
        }
      }
      
      OnConfirmedSaving -> {
        check(state is NewEntry)
        commands(SavePlainText(state.title, state.text))
      }
      
      is NotifyEntryCreated -> {
        check(state is NewEntry)
        state { ExistingEntry(plainTextItem = event.plainTextItem) }
        news(ShowEntryCreated)
      }
      
      OnTitleActionClicked -> {
        check(state is ExistingEntry)
        handleAction(
          itemProvider = state::plainTextItem,
          textState = state.titleState,
          stateResetAction = { titleState -> state.copy(titleState = titleState) },
          updateAction = { title -> copy(title = title) },
          updateCommand = ::UpdateTitle,
          allowEmptySave = false,
          copyNews = ShowTitleCopied,
          copyCommand = { text -> Copy(R.string.clipboard_title, text) },
          setTextNews = ::SetTitle
        )
      }
      
      OnTextActionClicked -> {
        check(state is ExistingEntry)
        handleAction(
          itemProvider = state::plainTextItem,
          textState = state.textState,
          stateResetAction = { textState -> state.copy(textState = textState) },
          updateAction = { text -> copy(text = text) },
          updateCommand = ::UpdateText,
          allowEmptySave = true,
          copyNews = ShowTextCopied,
          copyCommand = { text -> Copy(R.string.clipboard_text, text) },
          setTextNews = ::SetText
        )
      }
      
      is UpdatedTitle -> {
        check(state is ExistingEntry)
        state { ExistingEntry(plainTextItem = event.plainTextItem) }
      }
      
      is UpdatedText -> {
        check(state is ExistingEntry)
        state { ExistingEntry(plainTextItem = event.plainTextItem) }
      }
      
      OnDeleteClicked -> {
        check(state is ExistingEntry)
        state { state.copy(showConfirmDeleteDialog = true) }
      }
      
      OnConfirmedDeleting -> {
        check(state is ExistingEntry)
        state { state.copy(showLoadingDialog = true, showConfirmDeleteDialog = false) }
        commands(DeletePlainText(state.plainTextItem))
      }
      
      NotifyEntryDeleted -> {
        check(state is ExistingEntry)
        state { state.copy(showLoadingDialog = false) }
        commands(GoBack)
      }
      
      OnDialogHidden -> {
        when (state) {
          is NewEntry -> state { state.copy(showConfirmSaveDialog = false) }
          is ExistingEntry -> state { state.copy(showConfirmDeleteDialog = false) }
        }
      }
      
      OnBackPressed -> {
        when (state) {
          is NewEntry -> {
            when {
              state.showConfirmSaveDialog -> state { state.copy(showConfirmSaveDialog = false) }
              else -> commands(GoBack)
            }
          }
          
          is ExistingEntry -> {
            when {
              state.showConfirmDeleteDialog -> {
                state { state.copy(showConfirmDeleteDialog = false) }
              }
              
              state.isEditingSomething -> {
                state {
                  state.copy(
                    titleState = state.titleState.reset(),
                    textState = state.textState.reset(),
                  )
                }
                sendSetInitialTextsNews()
              }
              
              else -> {
                commands(GoBack)
              }
            }
          }
        }
      }
    }
  }
  
  private fun sendSetInitialTextsNews() {
    when (val state = state) {
      is NewEntry -> {
        news(SetTitle(state.title), SetText(state.text))
      }
      
      is ExistingEntry -> {
        news(SetTitle(state.titleState.initialText), SetText(state.textState.initialText))
      }
    }
  }
}
