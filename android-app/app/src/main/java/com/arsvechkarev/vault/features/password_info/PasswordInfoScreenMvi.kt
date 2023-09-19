package com.arsvechkarev.vault.features.password_info

import buisnesslogic.Password
import com.arsvechkarev.vault.features.common.TextState
import com.arsvechkarev.vault.features.common.model.PasswordItem

sealed interface PasswordInfoScreenEvent {
  
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
  class SavePasswordEventReceived(val password: Password) : PasswordInfoScreenUiEvent
}

sealed interface PasswordInfoScreenCommand {
  class Copy(
    val labelRes: Int,
    val text: String
  ) : PasswordInfoScreenCommand
  
  class OpenEditPasswordScreen(val password: Password) : PasswordInfoScreenCommand
  
  sealed class UpdateItem(val passwordItem: PasswordItem) : PasswordInfoScreenCommand {
    
    class UpdateWebsiteName(passwordItem: PasswordItem) : UpdateItem(passwordItem)
    class UpdateLogin(passwordItem: PasswordItem) : UpdateItem(passwordItem)
    class UpdatePassword(passwordItem: PasswordItem) : UpdateItem(passwordItem)
    class UpdateNotes(passwordItem: PasswordItem) : UpdateItem(passwordItem)
  }
  
  class DeletePasswordItem(val passwordItem: PasswordItem) : PasswordInfoScreenCommand
  
  sealed interface RouterCommand : PasswordInfoScreenCommand {
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

data class PasswordInfoState(
  val passwordItem: PasswordItem,
  val websiteNameState: TextState = TextState(passwordItem.websiteName),
  val loginState: TextState = TextState(passwordItem.login),
  val notesState: TextState = TextState(passwordItem.notes),
  val showDeletePasswordDialog: Boolean = false,
  val showLoadingDialog: Boolean = false,
) {
  
  val password get() = Password.create(passwordItem.password)
  
  val isEditingSomething
    get() = websiteNameState.isEditingNow
        || loginState.isEditingNow
        || notesState.isEditingNow
}
