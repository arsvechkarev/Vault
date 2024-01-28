package com.arsvechkarev.vault.features.main_list

import android.net.Uri
import com.arsvechkarev.vault.core.ListScreenState
import com.arsvechkarev.vault.features.common.model.EntryItem
import com.arsvechkarev.vault.recycler.DifferentiableItem

sealed interface MainListEvent {
  class UpdateData(val data: ListScreenState) : MainListEvent
  class UpdateSearchResult(val searchEntries: List<DifferentiableItem>) : MainListEvent
  class ExportedPasswords(val uri: Uri) : MainListEvent
  object NotifyShowUsernamesChanged : MainListEvent
  object RequestReloadImages : MainListEvent
  object NetworkAvailable : MainListEvent
  object MasterPasswordNull : MainListEvent
}

sealed interface MainListUiEvent : MainListEvent {
  object OnInit : MainListUiEvent
  object OnSearchActionClicked : MainListUiEvent
  class OnSearchTextChanged(val text: String) : MainListUiEvent
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
  class FilterEntries(val searchText: String) : MainListCommand
  class ExportPasswordsFile(val fileUriForExporting: Uri) : MainListCommand
  
  sealed interface RouterCommand : MainListCommand {
    class OpenScreen(val info: ScreenInfo) : RouterCommand
    object SwitchBackToLogin : RouterCommand
    object GoBack : RouterCommand
  }
}

sealed interface MainListNews {
  object ShowKeyboard : MainListNews
  object NotifyDatasetChanged : MainListNews
  object LaunchSelectExportFileActivity : MainListNews
  object LaunchSelectImportFileActivity : MainListNews
}

data class MainListState(
  val data: ListScreenState = ListScreenState.loading(),
  val errorLoadingImagesHappened: Boolean = false,
  val searchState: SearchState = SearchState(),
  val menuOpened: Boolean = false,
  val exportedFileUri: Uri? = null,
  val showEntryTypeDialog: Boolean = false,
  val showExportingFileDialog: Boolean = false,
  val showShareExportedFileDialog: Boolean = false,
)

data class SearchState(
  val inSearchMode: Boolean = false,
  val entries: List<DifferentiableItem> = emptyList(),
  val text: String = "",
) {
  
  fun asInitial(baseItems: List<DifferentiableItem>): SearchState {
    return SearchState(inSearchMode = true, entries = baseItems)
  }
  
  fun reset() = SearchState()
}

enum class MenuItemType {
  IMPORT_PASSWORDS,
  EXPORT_PASSWORDS,
  SETTINGS,
  NEW_ENTRY,
}

sealed interface ScreenInfo {
  class ImportPasswords(val selectedFileUri: Uri, val askForConfirmation: Boolean) : ScreenInfo
  class Password(val passwordEntry: EntryItem) : ScreenInfo
  class Note(val noteEntry: EntryItem) : ScreenInfo
  object Settings : ScreenInfo
  object NewPassword : ScreenInfo
  object NewNote : ScreenInfo
}

enum class EntryType {
  PASSWORD,
  NOTE
}
