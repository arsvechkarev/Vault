package com.arsvechkarev.vault.features.main_list

import android.net.Uri
import com.arsvechkarev.vault.core.ScreenState
import com.arsvechkarev.vault.features.common.model.EntryItem
import com.arsvechkarev.vault.recycler.DifferentiableItem

sealed interface MainListEvent {
  class UpdateData(val data: ScreenState<List<DifferentiableItem>>) : MainListEvent
  class ExportedPasswords(val uri: Uri) : MainListEvent
}

sealed interface MainListUiEvent : MainListEvent {
  object OnInit : MainListUiEvent
  object OnOpenMenuClicked : MainListUiEvent
  object OnCloseMenuClicked : MainListUiEvent
  object OnEntryTypeDialogHidden : MainListUiEvent
  object OnBackPressed : MainListUiEvent
  class OnListItemClicked(val item: EntryItem) : MainListUiEvent
  class OnMenuItemClicked(val itemType: MenuItemType) : MainListUiEvent
  class OnEntryTypeSelected(val type: EntryType) : MainListUiEvent
  class OnFileForExportSelected(val fileUriForExporting: Uri) : MainListUiEvent
  object OnHideShareExportedFileDialog : MainListUiEvent
}

sealed interface MainListCommand {
  
  object LoadData : MainListCommand
  
  class ExportPasswordsFile(val fileUriForExporting: Uri) : MainListCommand
  
  sealed interface RouterCommand : MainListCommand {
    class OpenScreen(val type: ScreenType) : RouterCommand
    class GoToCorrespondingInfoScreen(val entryItem: EntryItem) : RouterCommand
    object GoBack : RouterCommand
  }
}

sealed interface MainListNews {
  object LaunchSelectExportFileActivity : MainListNews
}

data class MainListState(
  val data: ScreenState<List<DifferentiableItem>> = ScreenState.loading(),
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

enum class ScreenType {
  IMPORT_PASSWORDS,
  SETTINGS,
  NEW_PASSWORD,
  NEW_PLAIN_TEXT,
}

enum class EntryType {
  PASSWORD,
  PLAIN_TEXT
}
