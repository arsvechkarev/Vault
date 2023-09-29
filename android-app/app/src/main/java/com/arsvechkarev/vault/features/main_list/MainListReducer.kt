package com.arsvechkarev.vault.features.main_list

import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.common.model.PasswordItem
import com.arsvechkarev.vault.features.common.model.PlainTextItem
import com.arsvechkarev.vault.features.main_list.MainListCommand.ExportPasswordsFile
import com.arsvechkarev.vault.features.main_list.MainListCommand.LoadData
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.OpenScreen
import com.arsvechkarev.vault.features.main_list.MainListEvent.ExportedPasswords
import com.arsvechkarev.vault.features.main_list.MainListEvent.ShowUsernamesChanged
import com.arsvechkarev.vault.features.main_list.MainListEvent.UpdateData
import com.arsvechkarev.vault.features.main_list.MainListNews.LaunchSelectExportFileActivity
import com.arsvechkarev.vault.features.main_list.MainListNews.LaunchSelectImportFileActivity
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnCloseMenuClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnEntryTypeDialogHidden
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnEntryTypeSelected
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnExportFileSelected
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnHideShareExportedFileDialog
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnImportFileSelected
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnInit
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnListItemClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnMenuItemClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnOpenMenuClicked
import com.arsvechkarev.vault.features.main_list.ScreenInfo.ImportPasswords
import com.arsvechkarev.vault.features.main_list.ScreenInfo.NewPassword
import com.arsvechkarev.vault.features.main_list.ScreenInfo.NewPlainText
import com.arsvechkarev.vault.features.main_list.ScreenInfo.Password
import com.arsvechkarev.vault.features.main_list.ScreenInfo.PlainText
import com.arsvechkarev.vault.features.main_list.ScreenInfo.Settings

class MainListReducer : DslReducer<MainListState, MainListEvent, MainListCommand, MainListNews>() {
  
  override fun dslReduce(event: MainListEvent) {
    when (event) {
      OnInit -> {
        commands(LoadData)
      }
      is OnListItemClicked -> {
        when (event.item) {
          is PasswordItem -> commands(OpenScreen(Password(event.item)))
          is PlainTextItem -> commands(OpenScreen(PlainText(event.item)))
        }
      }
      OnOpenMenuClicked -> {
        state { copy(menuOpened = true) }
      }
      OnCloseMenuClicked -> {
        state { copy(menuOpened = false) }
      }
      is OnMenuItemClicked -> {
        if (event.itemType != MenuItemType.NEW_ENTRY) {
          state { copy(menuOpened = false) }
        }
        when (event.itemType) {
          MenuItemType.IMPORT_PASSWORDS -> news(LaunchSelectImportFileActivity)
          MenuItemType.EXPORT_PASSWORDS -> news(LaunchSelectExportFileActivity)
          MenuItemType.SETTINGS -> commands(OpenScreen(Settings))
          MenuItemType.NEW_ENTRY -> state { copy(showEntryTypeDialog = true) }
        }
      }
      is OnEntryTypeSelected -> {
        state { copy(showEntryTypeDialog = false, menuOpened = false) }
        when (event.type) {
          EntryType.PASSWORD -> commands(OpenScreen(NewPassword))
          EntryType.PLAIN_TEXT -> commands(OpenScreen(NewPlainText))
        }
      }
      OnEntryTypeDialogHidden -> {
        state { copy(showEntryTypeDialog = false) }
      }
      OnBackPressed -> {
        when {
          state.showExportingFileDialog -> Unit // Exporting file in progress, do nothing
          state.showShareExportedFileDialog -> state { copy(showShareExportedFileDialog = false) }
          state.showEntryTypeDialog -> state { copy(showEntryTypeDialog = false) }
          state.menuOpened -> state { copy(menuOpened = false) }
          else -> commands(GoBack)
        }
      }
      is UpdateData -> {
        state { copy(data = event.data) }
      }
      is ShowUsernamesChanged -> {
        commands(LoadData)
      }
      is OnImportFileSelected -> {
        commands(OpenScreen(ImportPasswords(event.fileUri)))
      }
      is OnExportFileSelected -> {
        commands(ExportPasswordsFile(event.fileUri))
        state { copy(showExportingFileDialog = true) }
      }
      is ExportedPasswords -> {
        state {
          copy(
            showExportingFileDialog = false,
            showShareExportedFileDialog = true,
            exportedFileUri = event.uri
          )
        }
      }
      OnHideShareExportedFileDialog -> state { copy(showShareExportedFileDialog = false) }
    }
  }
}
