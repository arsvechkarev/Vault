package com.arsvechkarev.vault.features.plain_text_entry

import buisnesslogic.model.PlainTextEntryData
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.common.TextState
import com.arsvechkarev.vault.features.common.edit
import com.arsvechkarev.vault.features.common.extensions.handleAction
import com.arsvechkarev.vault.features.common.reset
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryCommand.Copy
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryCommand.DeletePlainTextEntry
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryCommand.FetchPlainTextEntry
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryCommand.GoBack
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryCommand.SavePlainTextEntry
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryCommand.UpdatePlainTextEntry.UpdateText
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryCommand.UpdatePlainTextEntry.UpdateTitle
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryEvent.NotifyEntryCreated
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryEvent.NotifyEntryDeleted
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryEvent.ReceivedPlainTextEntry
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryEvent.UpdatedPlainTextEntry.UpdatedText
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryEvent.UpdatedPlainTextEntry.UpdatedTitle
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryNews.SetText
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryNews.SetTitle
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryNews.ShowPlainTextEntryCreated
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryNews.ShowTextCopied
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryNews.ShowTitleCopied
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryState.ExistingEntry
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryState.NewEntry
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnConfirmedDeleting
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnConfirmedSaving
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnDeleteClicked
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnDialogHidden
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnInit
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnSaveClicked
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnTextActionClicked
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnTextChanged
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnTitleActionClicked
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnTitleChanged

class PlainTextEntryReducer : DslReducer<PlainTextEntryState, PlainTextEntryEvent,
    PlainTextEntryCommand, PlainTextEntryNews>() {
  
  override fun dslReduce(event: PlainTextEntryEvent) {
    val state = state
    when (event) {
      OnInit -> {
        if (state is ExistingEntry) {
          commands(FetchPlainTextEntry(state.plainTextId))
        }
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
        news(
          SetTitle(event.plainTextEntry.title),
          SetText(event.plainTextEntry.text)
        )
      }
      is OnTitleChanged -> when (state) {
        is NewEntry -> {
          state { state.copy(showTitleIsEmptyError = false, title = event.title) }
        }
        is ExistingEntry -> {
          state {
            state.copy(
              showTitleIsEmptyError = false,
              titleState = state.titleState.edit(event.title)
            )
          }
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
          state { state.copy(showTitleIsEmptyError = true) }
        } else {
          commands(SavePlainTextEntry(PlainTextEntryData(state.title, state.text)))
        }
      }
      OnConfirmedSaving -> {
        check(state is NewEntry)
      }
      is NotifyEntryCreated -> {
        check(state is NewEntry)
        state {
          ExistingEntry(
            plainTextId = event.plainTextEntry.id,
            plainTextEntry = event.plainTextEntry,
            titleState = TextState(event.plainTextEntry.title),
            textState = TextState(event.plainTextEntry.text),
          )
        }
        news(ShowPlainTextEntryCreated)
      }
      OnTitleActionClicked -> {
        check(state is ExistingEntry)
        handleAction(
          itemProvider = state::plainTextEntryNonNull,
          textState = state.titleState,
          updateTextAction = { titleState -> state.copy(titleState = titleState) },
          updateAction = { title -> copy(title = title) },
          updateCommand = ::UpdateTitle,
          allowEmptySave = false,
          showErrorIsEmptyAction = { state.copy(showTitleIsEmptyError = true) },
          copyNews = ShowTitleCopied,
          copyCommand = { text -> Copy(R.string.text_clipboard_label_title, text) },
          setTextNews = ::SetTitle
        )
      }
      OnTextActionClicked -> {
        check(state is ExistingEntry)
        handleAction(
          itemProvider = state::plainTextEntryNonNull,
          textState = state.textState,
          updateTextAction = { textState -> state.copy(textState = textState) },
          updateAction = { text -> copy(text = text) },
          updateCommand = ::UpdateText,
          allowEmptySave = true,
          copyNews = ShowTextCopied,
          copyCommand = { text -> Copy(R.string.text_clipboard_label_text, text) },
          setTextNews = ::SetText
        )
      }
      is UpdatedTitle -> {
        check(state is ExistingEntry)
        state {
          state.copy(
            plainTextId = event.plainTextEntry.id,
            plainTextEntry = event.plainTextEntry,
            titleState = TextState(event.plainTextEntry.title)
          )
        }
      }
      is UpdatedText -> {
        check(state is ExistingEntry)
        state {
          state.copy(
            plainTextId = event.plainTextEntry.id,
            plainTextEntry = event.plainTextEntry,
            textState = TextState(event.plainTextEntry.text)
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
        commands(DeletePlainTextEntry(state.plainTextId))
      }
      NotifyEntryDeleted -> {
        check(state is ExistingEntry)
        state { state.copy(showLoadingDialog = false) }
        commands(GoBack)
      }
      OnDialogHidden -> {
        if (state is ExistingEntry) {
          state { state.copy(showConfirmDeleteDialog = false) }
        }
      }
      OnBackPressed -> {
        when (state) {
          is NewEntry -> {
            commands(GoBack)
          }
          is ExistingEntry -> {
            when {
              state.showConfirmDeleteDialog -> {
                state { state.copy(showConfirmDeleteDialog = false) }
              }
              state.isEditingSomething -> {
                state {
                  state.copy(
                    showTitleIsEmptyError = false,
                    titleState = state.titleState.reset(),
                    textState = state.textState.reset(),
                  )
                }
                news(
                  SetTitle(state.titleState.initialText),
                  SetText(state.textState.initialText)
                )
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
}
