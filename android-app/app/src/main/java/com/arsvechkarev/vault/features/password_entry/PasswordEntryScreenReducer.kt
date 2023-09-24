package com.arsvechkarev.vault.features.password_entry

import buisnesslogic.model.PasswordEntry
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.common.TextState
import com.arsvechkarev.vault.features.common.edit
import com.arsvechkarev.vault.features.common.extensions.handleAction
import com.arsvechkarev.vault.features.common.reset
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.Copy
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.DeletePasswordEntry
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.FetchPasswordEntry
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.RouterCommand.GoToCreatingPasswordScreen
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.UpdatePasswordEntry.UpdateNotes
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.UpdatePasswordEntry.UpdatePassword
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.UpdatePasswordEntry.UpdateTitle
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.UpdatePasswordEntry.UpdateUsername
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.DeletedPasswordEntry
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.ReceivedPasswordEntry
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.UpdatedPasswordEntry.UpdatedNotes
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.UpdatedPasswordEntry.UpdatedPassword
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.UpdatedPasswordEntry.UpdatedTitle
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.UpdatedPasswordEntry.UpdatedUsername
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.SetNotes
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.SetTitle
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.SetUsername
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowNotesCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowPasswordCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowTitleCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowUsernameCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnConfirmedDeletion
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnCopyPasswordClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnDeleteClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnDialogHidden
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnInit
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnNotesActionClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnNotesTextChanged
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnOpenPasswordScreenClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnTitleActionClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnTitleTextChanged
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnUsernameActionClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnUsernameTextChanged
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.SavePasswordEntryEventReceived

class PasswordEntryScreenReducer :
  DslReducer<PasswordEntryState, PasswordEntryEvent, PasswordEntryCommand,
      PasswordEntryNews>() {
  
  override fun dslReduce(event: PasswordEntryEvent) {
    when (event) {
      OnInit -> {
        commands(FetchPasswordEntry(state.passwordId))
      }
      is ReceivedPasswordEntry -> {
        state {
          copy(
            passwordEntry = event.passwordEntry,
            titleState = TextState(event.passwordEntry.title),
            usernameState = TextState(event.passwordEntry.username),
            notesState = TextState(event.passwordEntry.notes)
          )
        }
        sendSetInitialTextsNews()
      }
      OnTitleActionClicked -> {
        handleAction(
          itemProvider = state::passwordEntryNonNull,
          textState = state.titleState,
          updateTextAction = { textState -> state.copy(titleState = textState) },
          updateAction = { text -> copy(title = text) },
          updateCommand = ::UpdateTitle,
          allowEmptySave = false,
          showErrorIsEmptyAction = { state.copy(showTitleIsEmptyError = true) },
          copyCommand = { text -> Copy(R.string.text_clipboard_label_title, text) },
          copyNews = ShowTitleCopied,
          setTextNews = ::SetTitle
        )
      }
      OnUsernameActionClicked -> {
        handleAction(
          itemProvider = state::passwordEntryNonNull,
          textState = state.usernameState,
          updateTextAction = { textState -> state.copy(usernameState = textState) },
          updateAction = { text -> copy(username = text) },
          updateCommand = ::UpdateUsername,
          allowEmptySave = true,
          copyCommand = { text -> Copy(R.string.text_clipboard_label_username, text) },
          copyNews = ShowUsernameCopied,
          setTextNews = ::SetUsername
        )
      }
      OnNotesActionClicked -> {
        handleAction(
          itemProvider = state::passwordEntryNonNull,
          textState = state.notesState,
          updateTextAction = { textState -> state.copy(notesState = textState) },
          updateAction = { text -> copy(notes = text) },
          updateCommand = ::UpdateNotes,
          allowEmptySave = true,
          copyCommand = { text -> Copy(R.string.text_clipboard_label_notes, text) },
          copyNews = ShowNotesCopied,
          setTextNews = ::SetNotes
        )
      }
      OnCopyPasswordClicked -> {
        val rawPassword = state.passwordEntryNonNull.password.stringData
        commands(Copy(R.string.text_clipboard_label_password, rawPassword))
        news(ShowPasswordCopied)
      }
      OnOpenPasswordScreenClicked -> {
        commands(GoToCreatingPasswordScreen(state.passwordEntryNonNull.password))
      }
      is OnTitleTextChanged -> {
        state {
          copy(
            titleState = titleState.edit(event.text),
            showTitleIsEmptyError = false,
          )
        }
      }
      is OnUsernameTextChanged -> {
        state { copy(usernameState = usernameState.edit(event.text)) }
      }
      is OnNotesTextChanged -> {
        state { copy(notesState = notesState.edit(event.text)) }
      }
      is SavePasswordEntryEventReceived -> {
        if (event.password != state.passwordEntryNonNull.password) {
          val item = state.passwordEntryNonNull.copy(password = event.password)
          commands(UpdatePassword(item))
        } else {
          commands(GoBack)
        }
      }
      OnDeleteClicked -> {
        state { copy(showDeletePasswordDialog = true) }
      }
      OnDialogHidden -> {
        state { copy(showDeletePasswordDialog = false) }
      }
      OnConfirmedDeletion -> {
        state { copy(showLoadingDialog = true, showDeletePasswordDialog = false) }
        commands(DeletePasswordEntry(state.passwordId))
      }
      OnBackPressed -> {
        when {
          state.showDeletePasswordDialog -> {
            state { copy(showDeletePasswordDialog = false) }
          }
          state.isEditingSomething -> {
            state {
              copy(
                showTitleIsEmptyError = false,
                titleState = titleState.reset(),
                usernameState = usernameState.reset(),
                notesState = notesState.reset()
              )
            }
            sendSetInitialTextsNews()
          }
          else -> commands(GoBack)
        }
      }
      is UpdatedTitle -> state { copy(passwordEntry = event.passwordEntry) }
      is UpdatedUsername -> state { copy(passwordEntry = event.passwordEntry) }
      is UpdatedNotes -> state { copy(passwordEntry = event.passwordEntry) }
      is UpdatedPassword -> {
        state { copy(passwordEntry = event.passwordEntry) }
        commands(GoBack)
      }
      DeletedPasswordEntry -> {
        commands(GoBack)
      }
    }
  }
  
  private fun sendSetInitialTextsNews() {
    news(
      SetTitle(state.passwordEntryNonNull.title),
      SetUsername(state.passwordEntryNonNull.username),
      SetNotes(state.passwordEntryNonNull.notes),
    )
  }
  
  private fun PasswordEntryState.update(
    passwordEntry: PasswordEntry,
    copyTextState: PasswordEntryState.() -> PasswordEntryState
  ): PasswordEntryState {
    return copyTextState(copy(passwordEntry = passwordEntry))
  }
}
