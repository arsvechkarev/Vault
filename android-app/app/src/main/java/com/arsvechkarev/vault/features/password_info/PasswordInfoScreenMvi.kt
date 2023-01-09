package com.arsvechkarev.vault.features.password_info

import com.arsvechkarev.vault.core.model.PasswordItem

sealed interface PasswordInfoScreenEvent {
  class SavePasswordEventReceived(val password: String) : PasswordInfoScreenEvent
  
  object DeletedPasswordInfo : PasswordInfoScreenEvent
  
  sealed interface UpdatedPasswordInfo : PasswordInfoScreenEvent {
    class UpdatedWebsiteName(val passwordItem: PasswordItem) : UpdatedPasswordInfo
    class UpdatedLogin(val passwordItem: PasswordItem) : UpdatedPasswordInfo
    class UpdatedPassword(val passwordItem: PasswordItem) : UpdatedPasswordInfo
    class UpdatedNotes(val passwordItem: PasswordItem) : UpdatedPasswordInfo
  }
}

sealed interface PasswordInfoScreenUiEvent : PasswordInfoScreenEvent {
  object OnInit : PasswordInfoScreenUiEvent
  object OnWebsiteNameActionClicked : PasswordInfoScreenUiEvent
  object OnLoginActionClicked : PasswordInfoScreenUiEvent
  object OnNotesActionClicked : PasswordInfoScreenUiEvent
  object OnOpenPasswordScreenClicked : PasswordInfoScreenUiEvent
  object OnCopyPasswordClicked : PasswordInfoScreenUiEvent
  object OnDeleteClicked : PasswordInfoScreenUiEvent
  object OnConfirmedDeletion : PasswordInfoScreenUiEvent
  object OnDialogHidden : PasswordInfoScreenUiEvent
  object OnBackPressed : PasswordInfoScreenUiEvent
  
  class OnWebsiteNameTextChanged(val text: String) : PasswordInfoScreenUiEvent
  class OnLoginTextChanged(val text: String) : PasswordInfoScreenUiEvent
  class OnNotesTextChanged(val text: String) : PasswordInfoScreenUiEvent
}

sealed interface InfoScreenCommand {
  class Copy(
    val labelRes: Int,
    val text: String
  ) : InfoScreenCommand
  
  class OpenEditPasswordScreen(val password: String) : InfoScreenCommand
  
  sealed class UpdateItem(val passwordItem: PasswordItem) : InfoScreenCommand {
    
    class UpdateWebsiteName(passwordItem: PasswordItem) : UpdateItem(passwordItem)
    class UpdateLogin(passwordItem: PasswordItem) : UpdateItem(passwordItem)
    class UpdatePassword(passwordItem: PasswordItem) : UpdateItem(passwordItem)
    class UpdateNotes(passwordItem: PasswordItem) : UpdateItem(passwordItem)
  }
  
  class DeletePasswordInfo(val passwordItem: PasswordItem) : InfoScreenCommand
  
  sealed interface RouterCommand : InfoScreenCommand {
    object GoToCreatePasswordScreen : RouterCommand
    object GoBack : RouterCommand
  }
}

sealed interface PasswordInfoScreenNews {
  class SetWebsiteName(val websiteName: String) : PasswordInfoScreenNews
  class SetLogin(val login: String) : PasswordInfoScreenNews
  class SetNotes(val notes: String) : PasswordInfoScreenNews
  object ShowWebsiteNameCopied : PasswordInfoScreenNews
  object ShowLoginCopied : PasswordInfoScreenNews
  object ShowPasswordCopied : PasswordInfoScreenNews
  object ShowNotesCopied : PasswordInfoScreenNews
}

data class PasswordInfoScreenState(
  val passwordItem: PasswordItem,
  val websiteNameState: TextState = TextState(passwordItem.websiteName),
  val loginState: TextState = TextState(passwordItem.login),
  val notesState: TextState = TextState(passwordItem.notes),
  val showDeletePasswordDialog: Boolean = false,
  val showLoadingDialog: Boolean = false,
) {
  
  val password get() = passwordItem.password
  
  val isEditingSomething
    get() = websiteNameState.isEditingNow
        || loginState.isEditingNow
        || notesState.isEditingNow
}

data class TextState(
  val initialText: String,
  val editedText: String = initialText,
  val isEditingNow: Boolean = false
)
