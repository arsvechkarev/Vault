package com.arsvechkarev.vault.features.note_entry

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.common.TextState
import com.arsvechkarev.vault.features.common.edit
import com.arsvechkarev.vault.features.common.extensions.handleAction
import com.arsvechkarev.vault.features.common.reset
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.Copy
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.DeleteNoteEntry
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.FetchNoteEntry
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.RouterCommand.SwitchBackToLogin
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.SaveNoteEntry
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.UpdateNoteEntry.UpdateIsFavorite
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.UpdateNoteEntry.UpdateText
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.UpdateNoteEntry.UpdateTitle
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent.MasterPasswordNull
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent.NotifyEntryCreated
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent.NotifyEntryDeleted
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent.ReceivedNoteEntry
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent.UpdatedNoteEntry.UpdatedIsFavorite
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent.UpdatedNoteEntry.UpdatedText
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent.UpdatedNoteEntry.UpdatedTitle
import com.arsvechkarev.vault.features.note_entry.NoteEntryNews.ShowNoteEntryCreated
import com.arsvechkarev.vault.features.note_entry.NoteEntryNews.ShowTextCopied
import com.arsvechkarev.vault.features.note_entry.NoteEntryNews.ShowTitleCopied
import com.arsvechkarev.vault.features.note_entry.NoteEntryState.ExistingEntry
import com.arsvechkarev.vault.features.note_entry.NoteEntryState.NewEntry
import com.arsvechkarev.vault.features.note_entry.NoteEntryUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.note_entry.NoteEntryUiEvent.OnConfirmedDeleting
import com.arsvechkarev.vault.features.note_entry.NoteEntryUiEvent.OnConfirmedSaving
import com.arsvechkarev.vault.features.note_entry.NoteEntryUiEvent.OnDeleteClicked
import com.arsvechkarev.vault.features.note_entry.NoteEntryUiEvent.OnDialogHidden
import com.arsvechkarev.vault.features.note_entry.NoteEntryUiEvent.OnFavoriteClicked
import com.arsvechkarev.vault.features.note_entry.NoteEntryUiEvent.OnInit
import com.arsvechkarev.vault.features.note_entry.NoteEntryUiEvent.OnSaveClicked
import com.arsvechkarev.vault.features.note_entry.NoteEntryUiEvent.OnTextActionClicked
import com.arsvechkarev.vault.features.note_entry.NoteEntryUiEvent.OnTextChanged
import com.arsvechkarev.vault.features.note_entry.NoteEntryUiEvent.OnTitleActionClicked
import com.arsvechkarev.vault.features.note_entry.NoteEntryUiEvent.OnTitleChanged
import domain.model.NoteEntryData

class NoteEntryReducer : DslReducer<NoteEntryState, NoteEntryEvent,
    NoteEntryCommand, NoteEntryNews>() {
  
  override fun dslReduce(event: NoteEntryEvent) {
    val state = state
    when (event) {
      OnInit -> {
        if (state is ExistingEntry) {
          commands(FetchNoteEntry(state.noteId))
        }
      }
      is ReceivedNoteEntry -> {
        check(state is ExistingEntry)
        state {
          state.copy(
            noteEntry = event.noteEntry,
            titleState = TextState(event.noteEntry.title),
            textState = TextState(event.noteEntry.text),
          )
        }
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
          val data = NoteEntryData(state.title, state.text, isFavorite = false)
          commands(SaveNoteEntry(data))
        }
      }
      OnConfirmedSaving -> {
        check(state is NewEntry)
      }
      OnTitleActionClicked -> {
        check(state is ExistingEntry)
        handleAction(
          itemProvider = state::noteEntryNonNull,
          textState = state.titleState,
          updateTextAction = { titleState -> state.copy(titleState = titleState) },
          updateAction = { title -> copy(title = title) },
          updateCommand = ::UpdateTitle,
          allowEmptySave = false,
          showErrorIsEmptyAction = { state.copy(showTitleIsEmptyError = true) },
          copyNews = ShowTitleCopied,
          copyCommand = { text -> Copy(R.string.text_clipboard_label_title, text) },
        )
      }
      OnTextActionClicked -> {
        check(state is ExistingEntry)
        handleAction(
          itemProvider = state::noteEntryNonNull,
          textState = state.textState,
          updateTextAction = { textState -> state.copy(textState = textState) },
          updateAction = { text -> copy(text = text) },
          updateCommand = ::UpdateText,
          allowEmptySave = true,
          copyNews = ShowTextCopied,
          copyCommand = { text -> Copy(R.string.text_clipboard_label_text, text) },
        )
      }
      OnFavoriteClicked -> {
        check(state is ExistingEntry)
        val noteEntry = state.noteEntry ?: return
        val newNoteEntry = noteEntry.copy(isFavorite = !state.noteEntry.isFavorite)
        commands(UpdateIsFavorite(newNoteEntry))
      }
      OnDeleteClicked -> {
        check(state is ExistingEntry)
        state { state.copy(showConfirmDeleteDialog = true) }
      }
      OnConfirmedDeleting -> {
        check(state is ExistingEntry)
        state { state.copy(showLoadingDialog = true, showConfirmDeleteDialog = false) }
        commands(DeleteNoteEntry(state.noteId))
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
              }
              else -> {
                commands(GoBack)
              }
            }
          }
        }
      }
      is NotifyEntryCreated -> {
        check(state is NewEntry)
        state {
          ExistingEntry(
            noteId = event.noteEntry.id,
            noteEntry = event.noteEntry,
            titleState = TextState(event.noteEntry.title),
            textState = TextState(event.noteEntry.text),
          )
        }
        news(ShowNoteEntryCreated)
      }
      is UpdatedTitle -> {
        check(state is ExistingEntry)
        state {
          state.copy(
            noteEntry = event.noteEntry,
            titleState = TextState(event.noteEntry.title)
          )
        }
      }
      is UpdatedText -> {
        check(state is ExistingEntry)
        state {
          state.copy(
            noteEntry = event.noteEntry,
            textState = TextState(event.noteEntry.text)
          )
        }
      }
      is UpdatedIsFavorite -> {
        check(state is ExistingEntry)
        state {
          state.copy(
            noteId = event.noteEntry.id,
            noteEntry = event.noteEntry,
          )
        }
      }
      NotifyEntryDeleted -> {
        check(state is ExistingEntry)
        state { state.copy(showLoadingDialog = false) }
        commands(GoBack)
      }
      MasterPasswordNull -> {
        commands(SwitchBackToLogin)
      }
    }
  }
}
