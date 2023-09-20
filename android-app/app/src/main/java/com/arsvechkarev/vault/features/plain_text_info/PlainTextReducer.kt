package com.arsvechkarev.vault.features.plain_text_info

import buisnesslogic.model.PlainTextEntryData
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.common.TextState
import com.arsvechkarev.vault.features.common.edit
import com.arsvechkarev.vault.features.common.extensions.handleAction
import com.arsvechkarev.vault.features.common.reset
import com.arsvechkarev.vault.features.plain_text_info.PlainTextCommand.Copy
import com.arsvechkarev.vault.features.plain_text_info.PlainTextCommand.DeletePlainText
import com.arsvechkarev.vault.features.plain_text_info.PlainTextCommand.GoBack
import com.arsvechkarev.vault.features.plain_text_info.PlainTextCommand.SavePlainText
import com.arsvechkarev.vault.features.plain_text_info.PlainTextCommand.UpdateItem.UpdateText
import com.arsvechkarev.vault.features.plain_text_info.PlainTextCommand.UpdateItem.UpdateTitle
import com.arsvechkarev.vault.features.plain_text_info.PlainTextEvent.NotifyEntryCreated
import com.arsvechkarev.vault.features.plain_text_info.PlainTextEvent.NotifyEntryDeleted
import com.arsvechkarev.vault.features.plain_text_info.PlainTextEvent.ReceivedPlainTextEntry
import com.arsvechkarev.vault.features.plain_text_info.PlainTextEvent.UpdatedPlainText.UpdatedText
import com.arsvechkarev.vault.features.plain_text_info.PlainTextEvent.UpdatedPlainText.UpdatedTitle
import com.arsvechkarev.vault.features.plain_text_info.PlainTextNews.SetText
import com.arsvechkarev.vault.features.plain_text_info.PlainTextNews.SetTitle
import com.arsvechkarev.vault.features.plain_text_info.PlainTextNews.ShowEntryCreated
import com.arsvechkarev.vault.features.plain_text_info.PlainTextNews.ShowTextCopied
import com.arsvechkarev.vault.features.plain_text_info.PlainTextNews.ShowTitleCopied
import com.arsvechkarev.vault.features.plain_text_info.PlainTextState.ExistingEntry
import com.arsvechkarev.vault.features.plain_text_info.PlainTextState.NewEntry
import com.arsvechkarev.vault.features.plain_text_info.PlainTextUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.plain_text_info.PlainTextUiEvent.OnConfirmedDeleting
import com.arsvechkarev.vault.features.plain_text_info.PlainTextUiEvent.OnConfirmedSaving
import com.arsvechkarev.vault.features.plain_text_info.PlainTextUiEvent.OnDeleteClicked
import com.arsvechkarev.vault.features.plain_text_info.PlainTextUiEvent.OnDialogHidden
import com.arsvechkarev.vault.features.plain_text_info.PlainTextUiEvent.OnInit
import com.arsvechkarev.vault.features.plain_text_info.PlainTextUiEvent.OnSaveClicked
import com.arsvechkarev.vault.features.plain_text_info.PlainTextUiEvent.OnTextActionClicked
import com.arsvechkarev.vault.features.plain_text_info.PlainTextUiEvent.OnTextChanged
import com.arsvechkarev.vault.features.plain_text_info.PlainTextUiEvent.OnTitleActionClicked
import com.arsvechkarev.vault.features.plain_text_info.PlainTextUiEvent.OnTitleChanged

class PlainTextReducer : DslReducer<PlainTextState, PlainTextEvent,
    PlainTextCommand, PlainTextNews>() {
  
  override fun dslReduce(event: PlainTextEvent) {
    val state = state
    when (event) {
      OnInit -> {
        sendSetInitialTextsNews()
      }
      
      is ReceivedPlainTextEntry -> {
        check(state is ExistingEntry)
        state {
          state.copy(
            plainTextEntry = event.plainTextEntry,
            titleState = TextState(event.plainTextEntry.title),
            textState = TextState(event.plainTextEntry.text),
          )
        }
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
        commands(SavePlainText(PlainTextEntryData(state.title, state.text)))
      }
      
      is NotifyEntryCreated -> {
        check(state is NewEntry)
        state {
          ExistingEntry(
            plainTextId = event.plainTextEntry.id,
            plainTextEntry = event.plainTextEntry
          )
        }
        news(ShowEntryCreated)
      }
      
      OnTitleActionClicked -> {
        check(state is ExistingEntry)
        handleAction(
          itemProvider = state::plainTextEntryNonNull,
          textState = state.titleState,
          stateResetAction = { titleState -> state.copy(titleState = titleState) },
          updateAction = { title -> copy(title = title) },
          updateCommand = ::UpdateTitle,
          allowEmptySave = false,
          copyNews = ShowTitleCopied,
          copyCommand = { text -> Copy(R.string.clipboard_label_title, text) },
          setTextNews = ::SetTitle
        )
      }
      
      OnTextActionClicked -> {
        check(state is ExistingEntry)
        handleAction(
          itemProvider = state::plainTextEntryNonNull,
          textState = state.textState,
          stateResetAction = { textState -> state.copy(textState = textState) },
          updateAction = { text -> copy(text = text) },
          updateCommand = ::UpdateText,
          allowEmptySave = true,
          copyNews = ShowTextCopied,
          copyCommand = { text -> Copy(R.string.clipboard_label_text, text) },
          setTextNews = ::SetText
        )
      }
      
      is UpdatedTitle -> {
        check(state is ExistingEntry)
        state {
          ExistingEntry(
            plainTextId = event.plainTextEntry.id,
            plainTextEntry = event.plainTextEntry
          )
        }
      }
      
      is UpdatedText -> {
        check(state is ExistingEntry)
        state {
          ExistingEntry(
            plainTextId = event.plainTextEntry.id,
            plainTextEntry = event.plainTextEntry
          )
        }
      }
      
      OnDeleteClicked -> {
        check(state is ExistingEntry)
        state { state.copy(showConfirmDeleteDialog = true) }
      }
      
      OnConfirmedDeleting -> {
        check(state is ExistingEntry)
        state { state.copy(showLoadingDialog = true, showConfirmDeleteDialog = false) }
        commands(DeletePlainText(state.plainTextId))
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
