package com.arsvechkarev.vault.features.info

import androidx.annotation.StringRes
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens
import com.arsvechkarev.vault.core.model.PasswordInfoItem
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.info.InfoScreenCommand.Copy
import com.arsvechkarev.vault.features.info.InfoScreenCommand.DeletePasswordInfo
import com.arsvechkarev.vault.features.info.InfoScreenCommand.OpenEditPasswordScreen
import com.arsvechkarev.vault.features.info.InfoScreenCommand.UpdateItem
import com.arsvechkarev.vault.features.info.InfoScreenCommand.UpdateItem.UpdateLogin
import com.arsvechkarev.vault.features.info.InfoScreenCommand.UpdateItem.UpdateNotes
import com.arsvechkarev.vault.features.info.InfoScreenCommand.UpdateItem.UpdatePassword
import com.arsvechkarev.vault.features.info.InfoScreenCommand.UpdateItem.UpdateWebsiteName
import com.arsvechkarev.vault.features.info.InfoScreenEvent.DeletedPasswordInfo
import com.arsvechkarev.vault.features.info.InfoScreenEvent.SavePasswordEventReceived
import com.arsvechkarev.vault.features.info.InfoScreenEvent.UpdatedInfo.UpdatedLogin
import com.arsvechkarev.vault.features.info.InfoScreenEvent.UpdatedInfo.UpdatedNotes
import com.arsvechkarev.vault.features.info.InfoScreenEvent.UpdatedInfo.UpdatedPassword
import com.arsvechkarev.vault.features.info.InfoScreenEvent.UpdatedInfo.UpdatedWebsiteName
import com.arsvechkarev.vault.features.info.InfoScreenNews.ShowLoginCopied
import com.arsvechkarev.vault.features.info.InfoScreenNews.ShowNotesCopied
import com.arsvechkarev.vault.features.info.InfoScreenNews.ShowPasswordCopied
import com.arsvechkarev.vault.features.info.InfoScreenNews.ShowWebsiteNameCopied
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnConfirmedDeletion
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnCopyPasswordClicked
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnDeleteClicked
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnDialogHidden
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnLoginActionClicked
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnLoginTextChanged
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnNotesActionClicked
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnNotesTextChanged
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnOpenPasswordScreenClicked
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnWebsiteNameActionClicked
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnWebsiteNameTextChanged

class InfoScreenReducer(
  private val router: Router
) : DslReducer<InfoScreenState, InfoScreenEvent, InfoScreenCommand, InfoScreenNews>() {
  
  override fun dslReduce(event: InfoScreenEvent) {
    when (event) {
      OnWebsiteNameActionClicked -> {
        handleAction(
          textState = state.websiteNameState,
          stateReset = { copy(websiteNameState = it) },
          itemCopy = { copy(websiteName = state.websiteNameState.editedText) },
          command = ::UpdateWebsiteName,
          allowEmptySave = false,
          copyLabelRes = R.string.clipboard_label_website,
          copyNews = ShowWebsiteNameCopied
        )
      }
      OnLoginActionClicked -> {
        handleAction(
          textState = state.loginState,
          stateReset = { copy(loginState = it) },
          itemCopy = { copy(login = state.loginState.editedText) },
          command = ::UpdateLogin,
          allowEmptySave = false,
          copyLabelRes = R.string.clipboard_label_login,
          copyNews = ShowLoginCopied
        )
      }
      OnNotesActionClicked -> {
        handleAction(
          textState = state.notesState,
          stateReset = { copy(notesState = it) },
          itemCopy = { copy(notes = state.notesState.editedText) },
          command = ::UpdateNotes,
          allowEmptySave = true,
          copyLabelRes = R.string.clipboard_label_notes,
          copyNews = ShowNotesCopied
        )
      }
      OnCopyPasswordClicked -> {
        handleCopy(R.string.clipboard_label_password, state.password, ShowPasswordCopied)
      }
      OnOpenPasswordScreenClicked -> {
        commands(OpenEditPasswordScreen(state.password))
        router.goForward(Screens.CreatingPasswordScreen)
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
        commands(DeletePasswordInfo(state.passwordInfoItem))
      }
      OnBackPressed -> {
        if (state.isEditingSomething) {
          state {
            copy(
              websiteNameState = websiteNameState.reset(),
              loginState = loginState.reset(),
              notesState = notesState.reset()
            )
          }
        } else if (state.showDeletePasswordDialog) {
          state { copy(showDeletePasswordDialog = false) }
        } else {
          router.goBack()
        }
      }
      is UpdatedWebsiteName -> {
        state {
          update(event.passwordInfoItem) {
            copy(websiteNameState = websiteNameState.update(event.passwordInfoItem.websiteName))
          }
        }
      }
      is UpdatedLogin -> {
        state {
          update(event.passwordInfoItem) {
            copy(loginState = loginState.update(event.passwordInfoItem.login))
          }
        }
      }
      is UpdatedNotes -> {
        state {
          update(event.passwordInfoItem) {
            copy(notesState = notesState.update(event.passwordInfoItem.notes))
          }
        }
      }
      is UpdatedPassword -> {
        state { copy(passwordInfoItem = event.passwordInfoItem) }
        router.goBack()
      }
      is SavePasswordEventReceived -> {
        if (event.password != state.password) {
          val item = state.passwordInfoItem.copy(password = event.password)
          commands(UpdatePassword(item))
        } else {
          router.goBack()
        }
      }
      DeletedPasswordInfo -> {
        router.goBack()
      }
    }
  }
  
  private fun handleAction(
    textState: TextState,
    stateReset: InfoScreenState.(TextState) -> InfoScreenState,
    itemCopy: PasswordInfoItem.() -> PasswordInfoItem,
    command: (PasswordInfoItem) -> UpdateItem,
    allowEmptySave: Boolean = false,
    @StringRes copyLabelRes: Int,
    copyNews: InfoScreenNews
  ) {
    with(textState) {
      if (isEditingNow) {
        if (!allowEmptySave && editedText.isBlank()) return
        if (editedText == initialText) {
          state { stateReset(state, textState.reset()) }
        } else {
          commands(command(itemCopy(state.passwordInfoItem)))
        }
      } else {
        handleCopy(copyLabelRes, editedText, copyNews)
      }
    }
  }
  
  private fun handleCopy(@StringRes labelRes: Int, text: String, news: InfoScreenNews) {
    commands(Copy(labelRes, text))
    news(news)
  }
  
  private fun InfoScreenState.update(
    passwordInfoItem: PasswordInfoItem,
    copyTextState: InfoScreenState.() -> InfoScreenState
  ): InfoScreenState {
    return copyTextState(copy(passwordInfoItem = passwordInfoItem))
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
