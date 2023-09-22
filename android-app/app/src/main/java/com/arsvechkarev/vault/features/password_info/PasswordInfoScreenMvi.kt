package com.arsvechkarev.vault.features.password_info

import buisnesslogic.Password
import buisnesslogic.model.PasswordEntry
import com.arsvechkarev.vault.features.common.TextState

sealed interface PasswordInfoScreenEvent {
  
  class ReceivedPasswordEntry(val passwordEntry: PasswordEntry) : PasswordInfoScreenEvent
  
  object DeletedPasswordInfo : PasswordInfoScreenEvent
  
  sealed interface UpdatedPasswordInfo : PasswordInfoScreenEvent {
    class UpdatedTitle(val passwordEntry: PasswordEntry) : UpdatedPasswordInfo
    class UpdatedUsername(val passwordEntry: PasswordEntry) : UpdatedPasswordInfo
    class UpdatedPassword(val passwordEntry: PasswordEntry) : UpdatedPasswordInfo
    class UpdatedNotes(val passwordEntry: PasswordEntry) : UpdatedPasswordInfo
  }
}

sealed interface PasswordInfoScreenUiEvent : PasswordInfoScreenEvent {
  object OnInit : PasswordInfoScreenUiEvent
  object OnTitleActionClicked : PasswordInfoScreenUiEvent
  object OnUsernameActionClicked : PasswordInfoScreenUiEvent
  object OnNotesActionClicked : PasswordInfoScreenUiEvent
  object OnOpenPasswordScreenClicked : PasswordInfoScreenUiEvent
  object OnCopyPasswordClicked : PasswordInfoScreenUiEvent
  object OnDeleteClicked : PasswordInfoScreenUiEvent
  object OnConfirmedDeletion : PasswordInfoScreenUiEvent
  object OnDialogHidden : PasswordInfoScreenUiEvent
  object OnBackPressed : PasswordInfoScreenUiEvent
  
  class OnTitleTextChanged(val text: String) : PasswordInfoScreenUiEvent
  class OnUsernameTextChanged(val text: String) : PasswordInfoScreenUiEvent
  class OnNotesTextChanged(val text: String) : PasswordInfoScreenUiEvent
  class SavePasswordEventReceived(val password: Password) : PasswordInfoScreenUiEvent
}

sealed interface PasswordInfoScreenCommand {
  
  class FetchPasswordEntry(val passwordId: String) : PasswordInfoScreenCommand
  
  class Copy(
    val labelRes: Int,
    val text: String
  ) : PasswordInfoScreenCommand
  
  
  sealed class UpdateItem(val passwordEntry: PasswordEntry) : PasswordInfoScreenCommand {
    class UpdateTitle(passwordEntry: PasswordEntry) : UpdateItem(passwordEntry)
    class UpdateUsername(passwordEntry: PasswordEntry) : UpdateItem(passwordEntry)
    class UpdatePassword(passwordEntry: PasswordEntry) : UpdateItem(passwordEntry)
    class UpdateNotes(passwordEntry: PasswordEntry) : UpdateItem(passwordEntry)
  }
  
  class DeletePasswordEntry(val passwordId: String) : PasswordInfoScreenCommand
  
  sealed interface RouterCommand : PasswordInfoScreenCommand {
    class GoToCreatePasswordScreen(val password: Password) : RouterCommand
    object GoBack : RouterCommand
  }
}

sealed interface PasswordInfoScreenNews {
  class SetTitle(val title: String) : PasswordInfoScreenNews
  class SetUsername(val username: String) : PasswordInfoScreenNews
  class SetNotes(val notes: String) : PasswordInfoScreenNews
  object ShowTitleCopied : PasswordInfoScreenNews
  object ShowUsernameCopied : PasswordInfoScreenNews
  object ShowPasswordCopied : PasswordInfoScreenNews
  object ShowNotesCopied : PasswordInfoScreenNews
}

data class PasswordInfoState(
  val passwordId: String,
  val passwordEntry: PasswordEntry? = null,
  val titleState: TextState = TextState.empty(),
  val usernameState: TextState = TextState.empty(),
  val notesState: TextState = TextState.empty(),
  val showDeletePasswordDialog: Boolean = false,
  val showLoadingDialog: Boolean = false,
) {
  
  val passwordEntryNonNull: PasswordEntry get() = requireNotNull(passwordEntry)
  
  val isEditingSomething
    get() = titleState.isEditingNow
        || usernameState.isEditingNow
        || notesState.isEditingNow
}
