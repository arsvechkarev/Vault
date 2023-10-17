package com.arsvechkarev.vault.features.password_entry

import com.arsvechkarev.vault.features.common.TextState
import com.arsvechkarev.vault.features.common.domain.CreatingPasswordMode
import domain.Password
import domain.model.PasswordEntry
import domain.model.PasswordEntryData

sealed interface PasswordEntryEvent {
  
  class ReceivedPasswordEntry(val passwordEntry: PasswordEntry) : PasswordEntryEvent
  class CreatedPasswordEntry(val passwordEntry: PasswordEntry) : PasswordEntryEvent
  class PasswordUpdated(val password: Password) : PasswordEntryEvent
  object DeletedPasswordEntry : PasswordEntryEvent
  object NetworkAvailable : PasswordEntryEvent
  object MasterPasswordNull : PasswordEntryEvent
  
  sealed interface UpdatedPasswordEntry : PasswordEntryEvent {
    class UpdatedTitle(val passwordEntry: PasswordEntry) : UpdatedPasswordEntry
    class UpdatedUsername(val passwordEntry: PasswordEntry) : UpdatedPasswordEntry
    class UpdatedPassword(val passwordEntry: PasswordEntry) : UpdatedPasswordEntry
    class UpdatedUrl(val passwordEntry: PasswordEntry) : UpdatedPasswordEntry
    class UpdatedNotes(val passwordEntry: PasswordEntry) : UpdatedPasswordEntry
    class UpdatedIsFavorite(val passwordEntry: PasswordEntry) : UpdatedPasswordEntry
  }
}

sealed interface PasswordEntryUiEvent : PasswordEntryEvent {
  object OnInit : PasswordEntryUiEvent
  object OnTitleActionClicked : PasswordEntryUiEvent
  object OnUsernameActionClicked : PasswordEntryUiEvent
  object OnOpenUrlClicked : PasswordEntryUiEvent
  object OnUrlActionClicked : PasswordEntryUiEvent
  object OnNotesActionClicked : PasswordEntryUiEvent
  object OnOpenPasswordScreenClicked : PasswordEntryUiEvent
  object OnCopyPasswordClicked : PasswordEntryUiEvent
  object OnDeleteClicked : PasswordEntryUiEvent
  object OnFavoriteClicked : PasswordEntryUiEvent
  object OnConfirmedDeletion : PasswordEntryUiEvent
  object OnHideDeleteDialog : PasswordEntryUiEvent
  object OnImagesLoadingFailed : PasswordEntryUiEvent
  object OnBackPressed : PasswordEntryUiEvent
  object OnEditTextTitleSubmitClicked : PasswordEntryUiEvent
  object OnEditTextUsernameSubmitClicked : PasswordEntryUiEvent
  object OnEditTextUrlSubmitClicked : PasswordEntryUiEvent
  object OnEditTextNotesSubmitClicked : PasswordEntryUiEvent
  object OnSaveClicked : PasswordEntryUiEvent
  class OnTitleTextChanged(val text: String) : PasswordEntryUiEvent
  class OnUsernameTextChanged(val text: String) : PasswordEntryUiEvent
  class OnUrlTextChanged(val text: String) : PasswordEntryUiEvent
  class OnNotesTextChanged(val text: String) : PasswordEntryUiEvent
}

sealed interface PasswordEntryCommand {
  
  class FetchPasswordEntry(val passwordId: String) : PasswordEntryCommand
  
  class Copy(
    val labelRes: Int,
    val text: String
  ) : PasswordEntryCommand
  
  class SavePassword(val passwordEntryData: PasswordEntryData) : PasswordEntryCommand
  class SetupPasswordCreatingScreen(val mode: CreatingPasswordMode) : PasswordEntryCommand
  
  sealed class UpdatePasswordEntry(val passwordEntry: PasswordEntry) : PasswordEntryCommand {
    class UpdateTitle(passwordEntry: PasswordEntry) : UpdatePasswordEntry(passwordEntry)
    class UpdateUsername(passwordEntry: PasswordEntry) : UpdatePasswordEntry(passwordEntry)
    class UpdatePassword(passwordEntry: PasswordEntry) : UpdatePasswordEntry(passwordEntry)
    class UpdateUrl(passwordEntry: PasswordEntry) : UpdatePasswordEntry(passwordEntry)
    class UpdateNotes(passwordEntry: PasswordEntry) : UpdatePasswordEntry(passwordEntry)
    class UpdateIsFavorite(passwordEntry: PasswordEntry) : UpdatePasswordEntry(passwordEntry)
  }
  
  class DeletePasswordEntry(val passwordId: String) : PasswordEntryCommand
  
  sealed interface RouterCommand : PasswordEntryCommand {
    object GoToCreatingPasswordScreen : RouterCommand
    object SwitchBackToLogin : RouterCommand
    object GoBack : RouterCommand
  }
}

sealed interface PasswordEntryNews {
  object ShowTitleCopied : PasswordEntryNews
  object ShowUsernameCopied : PasswordEntryNews
  object ShowUrlCopied : PasswordEntryNews
  object ShowPasswordCopied : PasswordEntryNews
  object ShowNotesCopied : PasswordEntryNews
  class ReloadTitleIcon(val title: String) : PasswordEntryNews
  object RequestUsernameFocus : PasswordEntryNews
  object RequestUrlFocus : PasswordEntryNews
  object RequestNotesFocus : PasswordEntryNews
  object SwitchToExistingEntry : PasswordEntryNews
  class OpenUrl(val url: String) : PasswordEntryNews
}

sealed interface PasswordEntryState {
  
  data class NewEntry(
    val title: String = "",
    val username: String = "",
    val password: Password = Password.empty(),
    val url: String = "",
    val notes: String = "",
    val showTitleEmptyError: Boolean = false,
    val errorLoadingImagesHappened: Boolean = false,
    val switchedToPasswordThroughSubmit: Boolean = false,
  ) : PasswordEntryState
  
  data class ExistingEntry(
    val passwordId: String,
    val passwordEntry: PasswordEntry? = null,
    val errorLoadingImagesHappened: Boolean = false,
    val titleState: TextState = TextState.empty(),
    val usernameState: TextState = TextState.empty(),
    val urlState: TextState = TextState.empty(),
    val notesState: TextState = TextState.empty(),
    val showTitleEmptyError: Boolean = false,
    val showDeletePasswordDialog: Boolean = false,
    val showLoadingDialog: Boolean = false,
  ) : PasswordEntryState {
    
    val passwordEntryNonNull: PasswordEntry get() = requireNotNull(passwordEntry)
    
    val isEditingSomething
      get() = titleState.isEditingNow
          || usernameState.isEditingNow
          || urlState.isEditingNow
          || notesState.isEditingNow
  }
}
