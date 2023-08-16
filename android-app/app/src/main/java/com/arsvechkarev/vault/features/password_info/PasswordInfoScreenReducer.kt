package com.arsvechkarev.vault.features.password_info

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.common.edit
import com.arsvechkarev.vault.features.common.extensions.handleAction
import com.arsvechkarev.vault.features.common.model.PasswordItem
import com.arsvechkarev.vault.features.common.reset
import com.arsvechkarev.vault.features.common.update
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.Copy
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.DeletePasswordItem
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.OpenEditPasswordScreen
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.RouterCommand.GoToCreatePasswordScreen
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.UpdateItem.UpdateLogin
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.UpdateItem.UpdateNotes
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.UpdateItem.UpdatePassword
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenCommand.UpdateItem.UpdateWebsiteName
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.DeletedPasswordInfo
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.UpdatedPasswordInfo.UpdatedLogin
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.UpdatedPasswordInfo.UpdatedNotes
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.UpdatedPasswordInfo.UpdatedPassword
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.UpdatedPasswordInfo.UpdatedWebsiteName
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenNews.SetLogin
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenNews.SetNotes
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenNews.SetWebsiteName
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenNews.ShowLoginCopied
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenNews.ShowNotesCopied
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenNews.ShowPasswordCopied
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenNews.ShowWebsiteNameCopied
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnConfirmedDeletion
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnCopyPasswordClicked
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnDeleteClicked
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnDialogHidden
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnInit
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnLoginActionClicked
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnLoginTextChanged
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnNotesActionClicked
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnNotesTextChanged
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnOpenPasswordScreenClicked
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnWebsiteNameActionClicked
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.OnWebsiteNameTextChanged
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenUiEvent.SavePasswordEventReceived

class PasswordInfoScreenReducer :
  DslReducer<PasswordInfoState, PasswordInfoScreenEvent, PasswordInfoScreenCommand,
      PasswordInfoScreenNews>() {
  
  override fun dslReduce(event: PasswordInfoScreenEvent) {
    when (event) {
      OnInit -> {
        sendSetInitialTextsNews()
      }
      
      OnWebsiteNameActionClicked -> {
        handleAction(
          itemProvider = state::passwordItem,
          textState = state.websiteNameState,
          stateResetAction = { textState -> state.copy(websiteNameState = textState) },
          updateAction = { text -> copy(websiteName = text) },
          updateCommand = ::UpdateWebsiteName,
          allowEmptySave = false,
          copyCommand = { text -> Copy(R.string.clipboard_label_website, text) },
          copyNews = ShowWebsiteNameCopied,
          setTextNews = ::SetWebsiteName
        )
      }
      
      OnLoginActionClicked -> {
        handleAction(
          itemProvider = state::passwordItem,
          textState = state.loginState,
          stateResetAction = { textState -> state.copy(loginState = textState) },
          updateAction = { text -> copy(login = text) },
          updateCommand = ::UpdateLogin,
          allowEmptySave = false,
          copyCommand = { text -> Copy(R.string.clipboard_label_login, text) },
          copyNews = ShowLoginCopied,
          setTextNews = ::SetLogin
        )
      }
      
      OnNotesActionClicked -> {
        handleAction(
          itemProvider = state::passwordItem,
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
        commands(Copy(R.string.clipboard_label_password, state.password))
        news(ShowPasswordCopied)
      }
      
      OnOpenPasswordScreenClicked -> {
        commands(
          OpenEditPasswordScreen(state.password),
          GoToCreatePasswordScreen,
        )
      }
      
      is OnWebsiteNameTextChanged -> {
        state { copy(websiteNameState = websiteNameState.edit(event.text)) }
      }
      
      is OnLoginTextChanged -> {
        state { copy(loginState = loginState.edit(event.text)) }
      }
      
      is OnNotesTextChanged -> {
        state { copy(notesState = notesState.edit(event.text)) }
      }
      
      is SavePasswordEventReceived -> {
        if (event.password != state.password) {
          val item = state.passwordItem.copy(password = event.password)
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
        commands(DeletePasswordItem(state.passwordItem))
      }
      
      OnBackPressed -> {
        when {
          state.showDeletePasswordDialog -> {
            state { copy(showDeletePasswordDialog = false) }
          }
          
          state.isEditingSomething -> {
            state {
              copy(
                websiteNameState = websiteNameState.reset(),
                loginState = loginState.reset(),
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
      
      is UpdatedWebsiteName -> {
        state {
          update(event.passwordItem) {
            copy(websiteNameState = websiteNameState.update(event.passwordItem.websiteName))
          }
        }
      }
      
      is UpdatedLogin -> {
        state {
          update(event.passwordItem) {
            copy(loginState = loginState.update(event.passwordItem.login))
          }
        }
      }
      
      is UpdatedNotes -> {
        state {
          update(event.passwordItem) {
            copy(notesState = notesState.update(event.passwordItem.notes))
          }
        }
      }
      
      is UpdatedPassword -> {
        state { copy(passwordItem = event.passwordItem) }
        commands(GoBack)
      }
      
      DeletedPasswordInfo -> {
        commands(GoBack)
      }
    }
  }
  
  private fun sendSetInitialTextsNews() {
    news(
      SetWebsiteName(state.passwordItem.websiteName),
      SetLogin(state.passwordItem.login),
      SetNotes(state.passwordItem.notes),
    )
  }
  
  private fun PasswordInfoState.update(
    passwordItem: PasswordItem,
    copyTextState: PasswordInfoState.() -> PasswordInfoState
  ): PasswordInfoState {
    return copyTextState(copy(passwordItem = passwordItem))
  }
}
