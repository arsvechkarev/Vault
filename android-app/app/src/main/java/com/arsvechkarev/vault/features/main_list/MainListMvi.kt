package com.arsvechkarev.vault.features.main_list

import android.net.Uri
import com.arsvechkarev.vault.core.ScreenState
import com.arsvechkarev.vault.features.common.model.EntryItem
import com.arsvechkarev.vault.recycler.DifferentiableItem

sealed interface MainListEvent {
  class UpdateData(val data: ScreenState<List<DifferentiableItem>>) : MainListEvent
  class ExportedPasswords(val uri: Uri) : MainListEvent
  object ShowUsernamesChanged : MainListEvent
  object RequestReloadImages : MainListEvent
  object NetworkAvailable : MainListEvent
  object MasterPasswordNull : MainListEvent
}

sealed interface MainListUiEvent : MainListEvent {
  object OnInit : MainListUiEvent
  object OnOpenMenuClicked : MainListUiEvent
  object OnCloseMenuClicked : MainListUiEvent
  object OnEntryTypeDialogHidden : MainListUiEvent
  object OnImagesLoadingFailed : MainListUiEvent
  class OnListItemClicked(val item: EntryItem) : MainListUiEvent
  class OnMenuItemClicked(val itemType: MenuItemType) : MainListUiEvent
  class OnEntryTypeSelected(val type: EntryType) : MainListUiEvent
  class OnImportFileSelected(val fileUri: Uri) : MainListUiEvent
  class OnExportFileSelected(val fileUri: Uri) : MainListUiEvent
  object OnBackPressed : MainListUiEvent
  object OnHideShareExportedFileDialog : MainListUiEvent
}

sealed interface MainListCommand {
  
  object LoadData : MainListCommand
  class ExportPasswordsFile(val fileUriForExporting: Uri) : MainListCommand
  
  sealed interface RouterCommand : MainListCommand {
    class OpenScreen(val info: ScreenInfo) : RouterCommand
    object SwitchBackToLogin : RouterCommand
    object GoBack : RouterCommand
  }
}

sealed interface MainListNews {
  object NotifyDatasetChanged : MainListNews
  object LaunchSelectExportFileActivity : MainListNews
  object LaunchSelectImportFileActivity : MainListNews
}

data class MainListState(
  val data: ScreenState<List<DifferentiableItem>> = ScreenState.loading(),
  val errorLoadingImagesHappened: Boolean = false,
  val menuOpened: Boolean = false,
  val exportedFileUri: Uri? = null,
  val showEntryTypeDialog: Boolean = false,
  val showExportingFileDialog: Boolean = false,
  val showShareExportedFileDialog: Boolean = false,
)

enum class MenuItemType {
  IMPORT_PASSWORDS,
  EXPORT_PASSWORDS,
  SETTINGS,
  NEW_ENTRY,
}

sealed interface ScreenInfo {
  class ImportPasswords(val selectedFileUri: Uri, val askForConfirmation: Boolean) : ScreenInfo
  class Password(val passwordEntry: EntryItem) : ScreenInfo
  class PlainText(val plainTextEntry: EntryItem) : ScreenInfo
  object Settings : ScreenInfo
  object NewPassword : ScreenInfo
  object NewPlainText : ScreenInfo
}

enum class EntryType {
  PASSWORD,
  PLAIN_TEXT
}
