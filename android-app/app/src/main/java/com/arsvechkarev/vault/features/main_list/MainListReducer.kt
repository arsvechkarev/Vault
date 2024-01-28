package com.arsvechkarev.vault.features.main_list

import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.common.model.PasswordItem
import com.arsvechkarev.vault.features.common.model.NoteItem
import com.arsvechkarev.vault.features.main_list.MainListCommand.ExportPasswordsFile
import com.arsvechkarev.vault.features.main_list.MainListCommand.FilterEntries
import com.arsvechkarev.vault.features.main_list.MainListCommand.LoadData
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.OpenScreen
import com.arsvechkarev.vault.features.main_list.MainListCommand.RouterCommand.SwitchBackToLogin
import com.arsvechkarev.vault.features.main_list.MainListEvent.ExportedPasswords
import com.arsvechkarev.vault.features.main_list.MainListEvent.MasterPasswordNull
import com.arsvechkarev.vault.features.main_list.MainListEvent.NetworkAvailable
import com.arsvechkarev.vault.features.main_list.MainListEvent.NotifyShowUsernamesChanged
import com.arsvechkarev.vault.features.main_list.MainListEvent.RequestReloadImages
import com.arsvechkarev.vault.features.main_list.MainListEvent.UpdateData
import com.arsvechkarev.vault.features.main_list.MainListEvent.UpdateSearchResult
import com.arsvechkarev.vault.features.main_list.MainListNews.LaunchSelectExportFileActivity
import com.arsvechkarev.vault.features.main_list.MainListNews.LaunchSelectImportFileActivity
import com.arsvechkarev.vault.features.main_list.MainListNews.NotifyDatasetChanged
import com.arsvechkarev.vault.features.main_list.MainListNews.ShowKeyboard
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnCloseMenuClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnEntryTypeDialogHidden
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnEntryTypeSelected
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnExportFileSelected
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnHideShareExportedFileDialog
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnImagesLoadingFailed
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnImportFileSelected
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnInit
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnListItemClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnMenuItemClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnOpenMenuClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnSearchActionClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnSearchTextChanged
import com.arsvechkarev.vault.features.main_list.ScreenInfo.ImportPasswords
import com.arsvechkarev.vault.features.main_list.ScreenInfo.NewPassword
import com.arsvechkarev.vault.features.main_list.ScreenInfo.NewNote
import com.arsvechkarev.vault.features.main_list.ScreenInfo.Password
import com.arsvechkarev.vault.features.main_list.ScreenInfo.Note
import com.arsvechkarev.vault.features.main_list.ScreenInfo.Settings

class MainListReducer : DslReducer<MainListState, MainListEvent, MainListCommand, MainListNews>() {
  
  override fun dslReduce(event: MainListEvent) {
    when (event) {
      OnInit -> {
        commands(LoadData)
      }
      OnSearchActionClicked -> {
        if (state.searchState.text.isNotEmpty()) {
          state { copy(searchState = searchState.asInitial(data.getSuccessItemsOrEmpty())) }
        } else if (state.searchState.inSearchMode) {
          state { copy(searchState = searchState.reset()) }
        } else {
          news(ShowKeyboard)
          state { copy(searchState = searchState.asInitial(data.getSuccessItemsOrEmpty())) }
        }
      }
      is OnSearchTextChanged -> {
        state { copy(searchState = searchState.copy(text = event.text)) }
        commands(FilterEntries(searchText = event.text))
      }
      is OnListItemClicked -> {
        when (event.item) {
          is PasswordItem -> commands(OpenScreen(Password(event.item)))
          is NoteItem -> commands(OpenScreen(Note(event.item)))
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
          state { copy(menuOpened = false, searchState = searchState.reset()) }
        }
        when (event.itemType) {
          MenuItemType.IMPORT_PASSWORDS -> news(LaunchSelectImportFileActivity)
          MenuItemType.EXPORT_PASSWORDS -> news(LaunchSelectExportFileActivity)
          MenuItemType.SETTINGS -> commands(OpenScreen(Settings))
          MenuItemType.NEW_ENTRY -> state { copy(showEntryTypeDialog = true) }
        }
      }
      is OnEntryTypeSelected -> {
        state {
          copy(
            showEntryTypeDialog = false,
            menuOpened = false,
            searchState = searchState.reset()
          )
        }
        when (event.type) {
          EntryType.PASSWORD -> commands(OpenScreen(NewPassword))
          EntryType.NOTE -> commands(OpenScreen(NewNote))
        }
      }
      OnEntryTypeDialogHidden -> {
        state { copy(showEntryTypeDialog = false) }
      }
      OnImagesLoadingFailed -> {
        state { copy(errorLoadingImagesHappened = true) }
      }
      OnBackPressed -> {
        when {
          state.showExportingFileDialog -> Unit // Exporting file in progress, do nothing
          state.showShareExportedFileDialog -> state { copy(showShareExportedFileDialog = false) }
          state.showEntryTypeDialog -> state { copy(showEntryTypeDialog = false) }
          state.menuOpened -> state { copy(menuOpened = false) }
          state.searchState.inSearchMode -> state { copy(searchState = searchState.reset()) }
          else -> commands(GoBack)
        }
      }
      is UpdateData -> {
        state { copy(data = event.data) }
        commands(FilterEntries(searchText = state.searchState.text))
      }
      is UpdateSearchResult -> {
        state { copy(searchState = searchState.copy(entries = event.searchEntries)) }
      }
      is NotifyShowUsernamesChanged -> {
        commands(LoadData)
      }
      RequestReloadImages -> {
        news(NotifyDatasetChanged)
      }
      is OnImportFileSelected -> {
        val info = ImportPasswords(event.fileUri, askForConfirmation = state.data.isNotEmpty)
        commands(OpenScreen(info))
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
      OnHideShareExportedFileDialog -> {
        state { copy(showShareExportedFileDialog = false) }
      }
      NetworkAvailable -> {
        if (state.errorLoadingImagesHappened) {
          state { copy(errorLoadingImagesHappened = false) }
          news(NotifyDatasetChanged)
        }
      }
      MasterPasswordNull -> {
        commands(SwitchBackToLogin)
      }
    }
  }
}
