package com.arsvechkarev.vault.features.password_info

import androidx.annotation.StringRes
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.model.PasswordItem
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.Copy
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.DeletePasswordInfo
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.OpenEditPasswordScreen
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.RouterCommand.GoToCreatePasswordScreen
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.UpdateItem
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.UpdateItem.UpdateLogin
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.UpdateItem.UpdateNotes
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.UpdateItem.UpdatePassword
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.UpdateItem.UpdateWebsiteName
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.DeletedPasswordInfo
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent.SavePasswordEventReceived
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

class PasswordInfoScreenReducer :
  DslReducer<PasswordInfoScreenState, PasswordInfoScreenEvent, InfoScreenCommand,
      PasswordInfoScreenNews>() {
  
  override fun dslReduce(event: PasswordInfoScreenEvent) {
    when (event) {
      OnInit -> {
        sendResetTextsNews()
      }
      OnWebsiteNameActionClicked -> {
        handleAction(
          textState = state.websiteNameState,
          stateResetAction = { state -> copy(websiteNameState = state) },
          setTextAction = { text -> copy(websiteName = text) },
          updateCommand = ::UpdateWebsiteName,
          allowEmptySave = false,
          copyLabelRes = R.string.clipboard_label_website,
          copyNews = ShowWebsiteNameCopied,
          setTextNews = ::SetWebsiteName
        )
      }
      OnLoginActionClicked -> {
        handleAction(
          textState = state.loginState,
          stateResetAction = { state -> copy(loginState = state) },
          setTextAction = { text -> copy(login = text) },
          updateCommand = ::UpdateLogin,
          allowEmptySave = false,
          copyLabelRes = R.string.clipboard_label_login,
          copyNews = ShowLoginCopied,
          setTextNews = ::SetLogin
        )
      }
      OnNotesActionClicked -> {
        handleAction(
          textState = state.notesState,
          stateResetAction = { state -> copy(notesState = state) },
          setTextAction = { text -> copy(notes = text) },
          updateCommand = ::UpdateNotes,
          allowEmptySave = true,
          copyLabelRes = R.string.clipboard_label_notes,
          copyNews = ShowNotesCopied,
          setTextNews = ::SetNotes
        )
      }
      OnCopyPasswordClicked -> {
        handleCopy(R.string.clipboard_label_password, state.password, ShowPasswordCopied)
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
      OnDeleteClicked -> {
        state { copy(showDeletePasswordDialog = true) }
      }
      OnDialogHidden -> {
        state { copy(showDeletePasswordDialog = false) }
      }
      OnConfirmedDeletion -> {
        state { copy(showLoadingDialog = true, showDeletePasswordDialog = false) }
        commands(DeletePasswordInfo(state.passwordItem))
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
            sendResetTextsNews()
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
      is SavePasswordEventReceived -> {
        if (event.password != state.password) {
          val item = state.passwordItem.copy(password = event.password)
          commands(UpdatePassword(item))
        } else {
          commands(GoBack)
        }
      }
      DeletedPasswordInfo -> {
        commands(GoBack)
      }
    }
  }
  
  private fun handleAction(
    textState: TextState,
    stateResetAction: PasswordInfoScreenState.(TextState) -> PasswordInfoScreenState,
    setTextAction: PasswordItem.(String) -> PasswordItem,
    updateCommand: (PasswordItem) -> UpdateItem,
    allowEmptySave: Boolean = false,
    @StringRes copyLabelRes: Int,
    copyNews: PasswordInfoScreenNews,
    setTextNews: (String) -> PasswordInfoScreenNews,
  ) {
    with(textState) {
      if (isEditingNow) {
        if (!allowEmptySave && editedText.isBlank()) {
          return
        }
        val trimmedText = editedText.trim()
        if (trimmedText == initialText) {
          state { stateResetAction(state, textState.reset()) }
        } else {
          commands(updateCommand(setTextAction(state.passwordItem, trimmedText)))
          news(setTextNews(trimmedText))
        }
      } else {
        handleCopy(copyLabelRes, editedText, copyNews)
      }
    }
  }
  
  private fun sendResetTextsNews() {
    news(
      SetWebsiteName(state.passwordItem.websiteName),
      SetLogin(state.passwordItem.login),
      SetNotes(state.passwordItem.notes),
    )
  }
  
  private fun handleCopy(@StringRes labelRes: Int, text: String, news: PasswordInfoScreenNews) {
    commands(Copy(labelRes, text))
    news(news)
  }
  
  private fun PasswordInfoScreenState.update(
    passwordItem: PasswordItem,
    copyTextState: PasswordInfoScreenState.() -> PasswordInfoScreenState
  ): PasswordInfoScreenState {
    return copyTextState(copy(passwordItem = passwordItem))
  }
  
  private fun TextState.reset(): TextState {
    return copy(editedText = initialText, isEditingNow = false)
  }
  
  private fun TextState.update(newText: String): TextState {
    return copy(initialText = newText, editedText = newText, isEditingNow = false)
  }
  
  private fun TextState.edit(newText: String): TextState {
    return copy(editedText = newText.trim(), isEditingNow = true)
  }
}
