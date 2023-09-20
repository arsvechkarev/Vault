package com.arsvechkarev.vault.features.password_info

import buisnesslogic.model.PasswordEntry
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.common.TextState
import com.arsvechkarev.vault.features.common.edit
import com.arsvechkarev.vault.features.common.extensions.handleAction
import com.arsvechkarev.vault.features.common.reset
import com.arsvechkarev.vault.features.common.update
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.Copy
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.DeletePasswordEntry
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.FetchPasswordEntry
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.OpenEditPasswordScreen
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.RouterCommand.GoToCreatePasswordScreen
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.UpdateItem.UpdateNotes
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.UpdateItem.UpdatePassword
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.UpdateItem.UpdateTitle
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.UpdateItem.UpdateUsername
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.DeletedPasswordInfo
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.ReceivedPasswordEntry
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.UpdatedPasswordInfo.UpdatedNotes
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.UpdatedPasswordInfo.UpdatedPassword
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.UpdatedPasswordInfo.UpdatedTitle
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.UpdatedPasswordInfo.UpdatedUsername
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenNews.SetNotes
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenNews.SetTitle
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenNews.SetUsername
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenNews.ShowNotesCopied
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenNews.ShowPasswordCopied
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenNews.ShowTitleCopied
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenNews.ShowUsernameCopied
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnConfirmedDeletion
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnCopyPasswordClicked
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnDeleteClicked
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnDialogHidden
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnInit
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnNotesActionClicked
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnNotesTextChanged
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnOpenPasswordScreenClicked
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnTitleActionClicked
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnTitleTextChanged
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnUsernameActionClicked
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnUsernameTextChanged
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.SavePasswordEventReceived

class PasswordInfoScreenReducer :
  DslReducer<PasswordInfoState, PasswordInfoScreenEvent, PasswordInfoScreenCommand,
      PasswordInfoScreenNews>() {
  
  override fun dslReduce(event: PasswordInfoScreenEvent) {
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
          stateResetAction = { textState -> state.copy(titleState = textState) },
          updateAction = { text -> copy(title = text) },
          updateCommand = ::UpdateTitle,
          allowEmptySave = false,
          copyCommand = { text -> Copy(R.string.clipboard_label_title, text) },
          copyNews = ShowTitleCopied,
          setTextNews = ::SetTitle
        )
      }
      
      OnUsernameActionClicked -> {
        handleAction(
          itemProvider = state::passwordEntryNonNull,
          textState = state.usernameState,
          stateResetAction = { textState -> state.copy(usernameState = textState) },
          updateAction = { text -> copy(username = text) },
          updateCommand = ::UpdateUsername,
          allowEmptySave = false,
          copyCommand = { text -> Copy(R.string.clipboard_label_username, text) },
          copyNews = ShowUsernameCopied,
          setTextNews = ::SetUsername
        )
      }
      
      OnNotesActionClicked -> {
        handleAction(
          itemProvider = state::passwordEntryNonNull,
          textState = state.notesState,
          stateResetAction = { textState -> state.copy(notesState = textState) },
          updateAction = { text -> copy(notes = text) },
          updateCommand = ::UpdateNotes,
          allowEmptySave = true,
          copyCommand = { text -> Copy(R.string.clipboard_label_notes, text) },
          copyNews = ShowNotesCopied,
          setTextNews = ::SetNotes
        )
      }
      
      OnCopyPasswordClicked -> {
        val rawPassword = state.passwordEntryNonNull.password.stringData
        commands(Copy(R.string.clipboard_label_password, rawPassword))
        news(ShowPasswordCopied)
      }
      
      OnOpenPasswordScreenClicked -> {
        commands(
          OpenEditPasswordScreen(state.passwordEntryNonNull.password),
          GoToCreatePasswordScreen,
        )
      }
      
      is OnTitleTextChanged -> {
        state { copy(titleState = titleState.edit(event.text)) }
      }
      
      is OnUsernameTextChanged -> {
        state { copy(usernameState = usernameState.edit(event.text)) }
      }
      
      is OnNotesTextChanged -> {
        state { copy(notesState = notesState.edit(event.text)) }
      }
      
      is SavePasswordEventReceived -> {
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
                titleState = titleState.reset(),
                usernameState = usernameState.reset(),
                notesState = notesState.reset()
              )
            }
            sendSetInitialTextsNews()
          }
          
          else -> {
            commands(GoBack)
          }
        }
      }
      
      is UpdatedTitle -> {
        state {
          update(event.passwordEntry) {
            copy(titleState = titleState.update(event.passwordEntry.title))
          }
        }
      }
      
      is UpdatedUsername -> {
        state {
          update(event.passwordEntry) {
            copy(usernameState = usernameState.update(event.passwordEntry.username))
          }
        }
      }
      
      is UpdatedNotes -> {
        state {
          update(event.passwordEntry) {
            copy(notesState = notesState.update(event.passwordEntry.notes))
          }
        }
      }
      
      is UpdatedPassword -> {
        state { copy(passwordEntry = event.passwordEntry) }
        commands(GoBack)
      }
      
      DeletedPasswordInfo -> {
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
  
  private fun PasswordInfoState.update(
    passwordEntry: PasswordEntry,
    copyTextState: PasswordInfoState.() -> PasswordInfoState
  ): PasswordInfoState {
    return copyTextState(copy(passwordEntry = passwordEntry))
  }
}
