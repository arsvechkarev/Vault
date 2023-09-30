package com.arsvechkarev.vault.features.password_entry

import buisnesslogic.Password
import buisnesslogic.model.PasswordEntry
import com.arsvechkarev.vault.features.common.TextState

sealed interface PasswordEntryEvent {
  
  class ReceivedPasswordEntry(val passwordEntry: PasswordEntry) : PasswordEntryEvent
  object DeletedPasswordEntry : PasswordEntryEvent
  object NetworkAvailable : PasswordEntryEvent
  
  sealed interface UpdatedPasswordEntry : PasswordEntryEvent {
    class UpdatedTitle(val passwordEntry: PasswordEntry) : UpdatedPasswordEntry
    class UpdatedUsername(val passwordEntry: PasswordEntry) : UpdatedPasswordEntry
    class UpdatedPassword(val passwordEntry: PasswordEntry) : UpdatedPasswordEntry
    class UpdatedNotes(val passwordEntry: PasswordEntry) : UpdatedPasswordEntry
  }
}

sealed interface PasswordEntryUiEvent : PasswordEntryEvent {
  object OnInit : PasswordEntryUiEvent
  object OnTitleActionClicked : PasswordEntryUiEvent
  object OnUsernameActionClicked : PasswordEntryUiEvent
  object OnNotesActionClicked : PasswordEntryUiEvent
  object OnOpenPasswordScreenClicked : PasswordEntryUiEvent
  object OnCopyPasswordClicked : PasswordEntryUiEvent
  object OnDeleteClicked : PasswordEntryUiEvent
  object OnConfirmedDeletion : PasswordEntryUiEvent
  object OnDialogHidden : PasswordEntryUiEvent
  object OnImagesLoadingFailed : PasswordEntryUiEvent
  object OnBackPressed : PasswordEntryUiEvent
  
  class OnTitleTextChanged(val text: String) : PasswordEntryUiEvent
  class OnUsernameTextChanged(val text: String) : PasswordEntryUiEvent
  class OnNotesTextChanged(val text: String) : PasswordEntryUiEvent
  class SavePasswordEntryEventReceived(val password: Password) : PasswordEntryUiEvent
}

sealed interface PasswordEntryCommand {
  
  class FetchPasswordEntry(val passwordId: String) : PasswordEntryCommand
  
  class Copy(
    val labelRes: Int,
    val text: String
  ) : PasswordEntryCommand
  
  
  sealed class UpdatePasswordEntry(val passwordEntry: PasswordEntry) : PasswordEntryCommand {
    class UpdateTitle(passwordEntry: PasswordEntry) : UpdatePasswordEntry(passwordEntry)
    class UpdateUsername(passwordEntry: PasswordEntry) : UpdatePasswordEntry(passwordEntry)
    class UpdatePassword(passwordEntry: PasswordEntry) : UpdatePasswordEntry(passwordEntry)
    class UpdateNotes(passwordEntry: PasswordEntry) : UpdatePasswordEntry(passwordEntry)
  }
  
  class DeletePasswordEntry(val passwordId: String) : PasswordEntryCommand
  
  sealed interface RouterCommand : PasswordEntryCommand {
    class GoToCreatingPasswordScreen(val password: Password) : RouterCommand
    object GoBack : RouterCommand
  }
}

sealed interface PasswordEntryNews {
  class SetTitle(val title: String) : PasswordEntryNews
  class SetUsername(val username: String) : PasswordEntryNews
  class SetNotes(val notes: String) : PasswordEntryNews
  object ShowTitleCopied : PasswordEntryNews
  object ShowUsernameCopied : PasswordEntryNews
  object ShowPasswordCopied : PasswordEntryNews
  object ShowNotesCopied : PasswordEntryNews
  class ReloadTitleIcon(val title: String) : PasswordEntryNews
}

data class PasswordEntryState(
  val passwordId: String,
  val passwordEntry: PasswordEntry? = null,
  val errorLoadingImagesHappened: Boolean = false,
  val titleState: TextState = TextState.empty(),
  val usernameState: TextState = TextState.empty(),
  val notesState: TextState = TextState.empty(),
  val showTitleIsEmptyError: Boolean = false,
  val showDeletePasswordDialog: Boolean = false,
  val showLoadingDialog: Boolean = false,
) {
  
  val passwordEntryNonNull: PasswordEntry get() = requireNotNull(passwordEntry)
  
  val isEditingSomething
    get() = titleState.isEditingNow
        || usernameState.isEditingNow
        || notesState.isEditingNow
}
