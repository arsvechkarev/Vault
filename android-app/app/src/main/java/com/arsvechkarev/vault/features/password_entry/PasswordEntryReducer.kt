package com.arsvechkarev.vault.features.password_entry

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.common.TextState
import com.arsvechkarev.vault.features.common.domain.CreatingPasswordMode.CreateNew
import com.arsvechkarev.vault.features.common.domain.CreatingPasswordMode.EditExisting
import com.arsvechkarev.vault.features.common.edit
import com.arsvechkarev.vault.features.common.extensions.handleAction
import com.arsvechkarev.vault.features.common.reset
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.Copy
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.DeletePasswordEntry
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.FetchPasswordEntry
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.RouterCommand.GoToCreatingPasswordScreen
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.RouterCommand.SwitchBackToLogin
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.SavePassword
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.SetupPasswordCreatingScreen
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.UpdatePasswordEntry.UpdateIsFavorite
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.UpdatePasswordEntry.UpdateNotes
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.UpdatePasswordEntry.UpdatePassword
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.UpdatePasswordEntry.UpdateTitle
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.UpdatePasswordEntry.UpdateUrl
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.UpdatePasswordEntry.UpdateUsername
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.CreatedPasswordEntry
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.DeletedPasswordEntry
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.MasterPasswordNull
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.NetworkAvailable
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.PasswordUpdated
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.ReceivedPasswordEntry
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.UpdatedPasswordEntry.UpdatedIsFavorite
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.UpdatedPasswordEntry.UpdatedNotes
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.UpdatedPasswordEntry.UpdatedPassword
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.UpdatedPasswordEntry.UpdatedTitle
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.UpdatedPasswordEntry.UpdatedUrl
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.UpdatedPasswordEntry.UpdatedUsername
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.OpenUrl
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ReloadTitleIcon
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.RequestNotesFocus
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.RequestUrlFocus
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.RequestUsernameFocus
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowNotesCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowPasswordCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowTitleCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowUrlCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowUsernameCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.SwitchToExistingEntry
import com.arsvechkarev.vault.features.password_entry.PasswordEntryState.ExistingEntry
import com.arsvechkarev.vault.features.password_entry.PasswordEntryState.NewEntry
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnConfirmedDeletion
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnCopyPasswordClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnDeleteClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnEditTextNotesSubmitClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnEditTextTitleSubmitClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnEditTextUrlSubmitClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnEditTextUsernameSubmitClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnFavoriteClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnHideDeleteDialog
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnImagesLoadingFailed
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnInit
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnNotesActionClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnNotesTextChanged
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnOpenPasswordScreenClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnOpenUrlClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnSaveClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnTitleActionClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnTitleTextChanged
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnUrlActionClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnUrlTextChanged
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnUsernameActionClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnUsernameTextChanged
import domain.Password
import domain.model.PasswordEntryData

class PasswordEntryReducer :
  DslReducer<PasswordEntryState, PasswordEntryEvent, PasswordEntryCommand, PasswordEntryNews>() {
  
  override fun dslReduce(event: PasswordEntryEvent) = state.let { state ->
    when (event) {
      OnInit -> {
        if (state is ExistingEntry) {
          commands(FetchPasswordEntry(state.passwordId))
        }
      }
      is ReceivedPasswordEntry -> {
        check(state is ExistingEntry)
        state {
          state.copy(
            passwordEntry = event.passwordEntry,
            titleState = TextState(event.passwordEntry.title),
            usernameState = TextState(event.passwordEntry.username),
            urlState = TextState(event.passwordEntry.url),
            notesState = TextState(event.passwordEntry.notes)
          )
        }
      }
      OnFavoriteClicked -> {
        check(state is ExistingEntry)
        val passwordEntry = state.passwordEntry ?: return
        val newPasswordEntry = passwordEntry.copy(isFavorite = !passwordEntry.isFavorite)
        commands(UpdateIsFavorite(newPasswordEntry))
      }
      OnDeleteClicked -> {
        check(state is ExistingEntry)
        state { state.copy(showDeletePasswordDialog = true) }
      }
      OnHideDeleteDialog -> {
        check(state is ExistingEntry)
        state { state.copy(showDeletePasswordDialog = false) }
      }
      OnConfirmedDeletion -> {
        check(state is ExistingEntry)
        state { state.copy(showLoadingDialog = true, showDeletePasswordDialog = false) }
        commands(DeletePasswordEntry(state.passwordId))
      }
      is OnTitleTextChanged -> {
        when (state) {
          is NewEntry -> {
            state { state.copy(title = event.text, showTitleEmptyError = false) }
          }
          is ExistingEntry -> {
            state {
              state.copy(
                titleState = state.titleState.edit(event.text),
                showTitleEmptyError = false,
              )
            }
          }
        }
      }
      is OnUsernameTextChanged -> {
        when (state) {
          is NewEntry -> {
            state { state.copy(username = event.text) }
          }
          is ExistingEntry -> {
            state { state.copy(usernameState = state.usernameState.edit(event.text)) }
          }
        }
      }
      is OnUrlTextChanged -> {
        when (state) {
          is NewEntry -> {
            state { state.copy(url = event.text) }
          }
          is ExistingEntry -> {
            state { state.copy(urlState = state.urlState.edit(event.text)) }
          }
        }
      }
      is OnNotesTextChanged -> {
        when (state) {
          is NewEntry -> {
            state { state.copy(notes = event.text) }
          }
          is ExistingEntry -> {
            state { state.copy(notesState = state.notesState.edit(event.text)) }
          }
        }
      }
      OnEditTextTitleSubmitClicked -> {
        when (state) {
          is NewEntry -> news(RequestUsernameFocus)
          is ExistingEntry -> handleTitleAction(state)
        }
      }
      OnEditTextUsernameSubmitClicked -> {
        when (state) {
          is NewEntry -> {
            handleNewEntryGoToPasswordScreen(state.password)
            state { state.copy(switchedToPasswordThroughSubmit = true) }
          }
          is ExistingEntry -> handleTitleAction(state)
        }
      }
      OnEditTextUrlSubmitClicked -> {
        when (state) {
          is NewEntry -> news(RequestNotesFocus)
          is ExistingEntry -> handleTitleAction(state)
        }
      }
      OnEditTextNotesSubmitClicked -> {
        when (state) {
          is NewEntry -> handleOnContinueClicked(state)
          is ExistingEntry -> handleNotesAction(state)
        }
      }
      OnTitleActionClicked -> {
        check(state is ExistingEntry)
        handleTitleAction(state)
      }
      OnUsernameActionClicked -> {
        check(state is ExistingEntry)
        handleUsernameAction(state)
      }
      OnCopyPasswordClicked -> {
        check(state is ExistingEntry)
        val rawPassword = state.passwordEntryNonNull.password.stringData
        commands(Copy(R.string.text_clipboard_label_password, rawPassword))
        news(ShowPasswordCopied)
      }
      OnOpenPasswordScreenClicked -> {
        when (state) {
          is NewEntry -> {
            handleNewEntryGoToPasswordScreen(state.password)
          }
          is ExistingEntry -> {
            commands(
              SetupPasswordCreatingScreen(EditExisting(state.passwordEntryNonNull.password)),
              GoToCreatingPasswordScreen
            )
          }
        }
      }
      OnOpenUrlClicked -> {
        check(state is ExistingEntry)
        news(OpenUrl(state.urlState.editedText))
      }
      OnUrlActionClicked -> {
        check(state is ExistingEntry)
        handleUrlAction(state)
      }
      OnNotesActionClicked -> {
        check(state is ExistingEntry)
        handleNotesAction(state)
      }
      is PasswordUpdated -> {
        when (state) {
          is NewEntry -> {
            if (state.switchedToPasswordThroughSubmit) {
              news(RequestUrlFocus)
            }
            state {
              state.copy(
                password = event.password,
                switchedToPasswordThroughSubmit = false
              )
            }
            commands(GoBack)
          }
          is ExistingEntry -> {
            if (event.password != state.passwordEntryNonNull.password) {
              val item = state.passwordEntryNonNull.copy(password = event.password)
              commands(UpdatePassword(item), GoBack)
            } else {
              commands(GoBack)
            }
          }
        }
      }
      OnSaveClicked -> {
        check(state is NewEntry)
        handleOnContinueClicked(state)
      }
      OnImagesLoadingFailed -> {
        when (state) {
          is NewEntry -> state { state.copy(errorLoadingImagesHappened = true) }
          is ExistingEntry -> state { state.copy(errorLoadingImagesHappened = true) }
        }
      }
      OnBackPressed -> {
        when (state) {
          is ExistingEntry -> {
            when {
              state.showDeletePasswordDialog -> {
                state { state.copy(showDeletePasswordDialog = false) }
              }
              state.isEditingSomething -> {
                state {
                  state.copy(
                    showTitleEmptyError = false,
                    titleState = state.titleState.reset(),
                    usernameState = state.usernameState.reset(),
                    urlState = state.urlState.reset(),
                    notesState = state.notesState.reset()
                  )
                }
              }
              else -> commands(GoBack)
            }
          }
          is NewEntry -> {
            commands(GoBack)
          }
        }
      }
      is CreatedPasswordEntry -> {
        check(state is NewEntry)
        news(SwitchToExistingEntry)
        state {
          ExistingEntry(
            passwordId = event.passwordEntry.id,
            passwordEntry = event.passwordEntry,
            errorLoadingImagesHappened = state.errorLoadingImagesHappened,
            titleState = TextState(event.passwordEntry.title),
            usernameState = TextState(event.passwordEntry.username),
            urlState = TextState(event.passwordEntry.url),
            notesState = TextState(event.passwordEntry.notes),
          )
        }
      }
      is UpdatedIsFavorite -> {
        check(state is ExistingEntry)
        state { state.copy(passwordEntry = event.passwordEntry) }
      }
      is UpdatedTitle -> {
        check(state is ExistingEntry)
        state { state.copy(passwordEntry = event.passwordEntry) }
      }
      is UpdatedUsername -> {
        check(state is ExistingEntry)
        state { state.copy(passwordEntry = event.passwordEntry) }
      }
      is UpdatedPassword -> {
        check(state is ExistingEntry)
        state { state.copy(passwordEntry = event.passwordEntry) }
      }
      is UpdatedUrl -> {
        check(state is ExistingEntry)
        state { state.copy(passwordEntry = event.passwordEntry) }
      }
      is UpdatedNotes -> {
        check(state is ExistingEntry)
        state { state.copy(passwordEntry = event.passwordEntry) }
      }
      DeletedPasswordEntry -> {
        commands(GoBack)
      }
      NetworkAvailable -> {
        when (state) {
          is NewEntry -> {
            state { state.copy(errorLoadingImagesHappened = true) }
            news(ReloadTitleIcon(state.title))
          }
          is ExistingEntry -> {
            state { state.copy(errorLoadingImagesHappened = true) }
            news(ReloadTitleIcon(state.titleState.editedText))
          }
        }
      }
      MasterPasswordNull -> {
        commands(SwitchBackToLogin)
      }
    }
  }
  
  private fun handleNewEntryGoToPasswordScreen(password: Password) {
    if (password.isNotEmpty) {
      commands(
        SetupPasswordCreatingScreen(EditExisting(password)),
        GoToCreatingPasswordScreen
      )
    } else {
      commands(
        SetupPasswordCreatingScreen(CreateNew),
        GoToCreatingPasswordScreen
      )
    }
  }
  
  private fun handleOnContinueClicked(state: NewEntry) {
    if (state.title.isBlank()) {
      state { state.copy(showTitleEmptyError = true) }
      return
    }
    val passwordEntryData = PasswordEntryData(
      title = state.title,
      username = state.username,
      password = state.password,
      url = state.url,
      notes = state.notes,
      isFavorite = false
    )
    commands(SavePassword(passwordEntryData))
  }
  
  private fun handleTitleAction(state: ExistingEntry) {
    handleAction(
      itemProvider = state::passwordEntryNonNull,
      textState = state.titleState,
      updateTextAction = { textState -> state.copy(titleState = textState) },
      updateAction = { text -> copy(title = text) },
      updateCommand = ::UpdateTitle,
      allowEmptySave = false,
      showErrorIsEmptyAction = { state.copy(showTitleEmptyError = true) },
      copyCommand = { text -> Copy(R.string.text_clipboard_label_title, text) },
      copyNews = ShowTitleCopied,
    )
  }
  
  private fun handleUsernameAction(state: ExistingEntry) {
    handleAction(
      itemProvider = state::passwordEntryNonNull,
      textState = state.usernameState,
      updateTextAction = { textState -> state.copy(usernameState = textState) },
      updateAction = { text -> copy(username = text) },
      updateCommand = ::UpdateUsername,
      allowEmptySave = true,
      copyCommand = { text -> Copy(R.string.text_clipboard_label_username, text) },
      copyNews = ShowUsernameCopied,
    )
  }
  
  private fun handleUrlAction(state: ExistingEntry) {
    handleAction(
      itemProvider = state::passwordEntryNonNull,
      textState = state.urlState,
      updateTextAction = { textState -> state.copy(urlState = textState) },
      updateAction = { text -> copy(url = text) },
      updateCommand = ::UpdateUrl,
      allowEmptySave = true,
      copyCommand = { text -> Copy(R.string.text_clipboard_label_url, text) },
      copyNews = ShowUrlCopied,
    )
  }
  
  private fun handleNotesAction(state: ExistingEntry) {
    handleAction(
      itemProvider = state::passwordEntryNonNull,
      textState = state.notesState,
      updateTextAction = { textState -> state.copy(notesState = textState) },
      updateAction = { text -> copy(notes = text) },
      updateCommand = ::UpdateNotes,
      allowEmptySave = true,
      copyCommand = { text -> Copy(R.string.text_clipboard_label_notes, text) },
      copyNews = ShowNotesCopied,
    )
  }
}
