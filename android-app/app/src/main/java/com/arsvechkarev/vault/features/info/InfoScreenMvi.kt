package com.arsvechkarev.vault.features.info

import com.arsvechkarev.vault.core.model.PasswordInfoItem

sealed interface InfoScreenEvent {
  class SavePasswordEventReceived(val password: String) : InfoScreenEvent
  
  object DeletedPasswordInfo : InfoScreenEvent
  
  sealed interface UpdatedInfo : InfoScreenEvent {
    class UpdatedWebsiteName(val passwordInfoItem: PasswordInfoItem) : UpdatedInfo
    class UpdatedLogin(val passwordInfoItem: PasswordInfoItem) : UpdatedInfo
    class UpdatedPassword(val passwordInfoItem: PasswordInfoItem) : UpdatedInfo
    class UpdatedNotes(val passwordInfoItem: PasswordInfoItem) : UpdatedInfo
  }
}

sealed interface InfoScreenUiEvent : InfoScreenEvent {
  object OnWebsiteNameActionClicked : InfoScreenUiEvent
  object OnLoginActionClicked : InfoScreenUiEvent
  object OnOpenPasswordScreenClicked : InfoScreenUiEvent
  object OnCopyPasswordClicked : InfoScreenUiEvent
  object OnNotesActionClicked : InfoScreenUiEvent
  object OnDeleteClicked : InfoScreenUiEvent
  object OnConfirmedDeletion : InfoScreenUiEvent
  object OnDialogHidden : InfoScreenUiEvent
  object OnBackPressed : InfoScreenUiEvent
  
  class OnWebsiteNameTextChanged(val text: String) : InfoScreenUiEvent
  class OnLoginTextChanged(val text: String) : InfoScreenUiEvent
  class OnNotesTextChanged(val text: String) : InfoScreenUiEvent
}

sealed interface InfoScreenCommand {
  class Copy(
    val labelRes: Int,
    val text: String
  ) : InfoScreenCommand
  
  class OpenEditPasswordScreen(val password: String) : InfoScreenCommand
  
  sealed class UpdateItem(val passwordInfoItem: PasswordInfoItem) : InfoScreenCommand {
    
    class UpdateWebsiteName(passwordInfoItem: PasswordInfoItem) : UpdateItem(passwordInfoItem)
    class UpdateLogin(passwordInfoItem: PasswordInfoItem) : UpdateItem(passwordInfoItem)
    class UpdatePassword(passwordInfoItem: PasswordInfoItem) : UpdateItem(passwordInfoItem)
    class UpdateNotes(passwordInfoItem: PasswordInfoItem) : UpdateItem(passwordInfoItem)
  }
  
  class DeletePasswordInfo(val passwordInfoItem: PasswordInfoItem) : InfoScreenCommand
}

sealed interface InfoScreenNews {
  object ShowWebsiteNameCopied : InfoScreenNews
  object ShowLoginCopied : InfoScreenNews
  object ShowPasswordCopied : InfoScreenNews
  object ShowNotesCopied : InfoScreenNews
}

data class InfoScreenState(
  val passwordInfoItem: PasswordInfoItem,
  val websiteNameState: TextState =
      TextState(passwordInfoItem.websiteName, passwordInfoItem.websiteName),
  val loginState: TextState =
      TextState(passwordInfoItem.login, passwordInfoItem.login),
  val notesState: TextState =
      TextState(passwordInfoItem.notes, passwordInfoItem.notes),
  val showDeletePasswordDialog: Boolean = false,
  val showLoadingDialog: Boolean = false,
) {
  
  val password get() = passwordInfoItem.password
  
  val isEditingSomething
    get() = websiteNameState.isEditingNow
        || loginState.isEditingNow
        || notesState.isEditingNow
}

data class TextState(
  val initialText: String,
  val editedText: String,
  val isEditingNow: Boolean = false
)
