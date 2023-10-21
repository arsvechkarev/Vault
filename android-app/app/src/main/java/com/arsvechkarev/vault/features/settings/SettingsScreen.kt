package com.arsvechkarev.vault.features.settings

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.extensions.toReadablePath
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.core.views.SettingsItem
import com.arsvechkarev.vault.core.views.SettingsItem.Companion.SettingsItem
import com.arsvechkarev.vault.core.views.Snackbar.Companion.Snackbar
import com.arsvechkarev.vault.core.views.Snackbar.Companion.snackbar
import com.arsvechkarev.vault.core.views.Snackbar.Type.CHECKMARK
import com.arsvechkarev.vault.core.views.Snackbar.Type.ERROR
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.features.common.biometrics.BiometricsDialog
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Companion.EnterPasswordDialog
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Companion.enterPasswordDialog
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Mode.CheckingMasterPassword
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.features.common.dialogs.LoadingDialog
import com.arsvechkarev.vault.features.common.dialogs.loadingDialog
import com.arsvechkarev.vault.features.common.extensions.setStatusBarColor
import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.HIDDEN
import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.HIDDEN_KEEPING_KEYBOARD
import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.SHOWN
import com.arsvechkarev.vault.features.settings.SettingsBiometricsError.LOCKOUT
import com.arsvechkarev.vault.features.settings.SettingsBiometricsError.LOCKOUT_PERMANENT
import com.arsvechkarev.vault.features.settings.SettingsBiometricsError.OTHER
import com.arsvechkarev.vault.features.settings.SettingsNews.LaunchFolderSelection
import com.arsvechkarev.vault.features.settings.SettingsNews.SetBiometricsEnabled
import com.arsvechkarev.vault.features.settings.SettingsNews.SetShowUsernames
import com.arsvechkarev.vault.features.settings.SettingsNews.SetStorageBackupEnabled
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowBackupPerformed
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowBiometricsEnabled
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowBiometricsError
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowBiometricsPrompt
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowImagesCacheCleared
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowMasterPasswordChanged
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowStorageBackupEnabled
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnAppearedOnScreen
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnBackupNowClicked
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnBiometricsEvent
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnChangeMasterPasswordClicked
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnClearImagesCacheClicked
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnEnableBiometricsChanged
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnEnableStorageBackupChanged
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnEnteredPasswordToChangeMasterPassword
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnHideEnableBiometricsDialog
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnHideEnterPasswordDialog
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnInit
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnMasterPasswordChangedReceived
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnProceedEnableBiometricsDialog
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnSelectBackupFolderClicked
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnSelectedBackupFolder
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnShowUsernamesChanged
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.DividerHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.GradientDrawableHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginMedium
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.IntSize
import viewdsl.backgroundColor
import viewdsl.constraints
import viewdsl.id
import viewdsl.isVisible
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.margins
import viewdsl.onClick
import viewdsl.paddings
import viewdsl.text
import viewdsl.textSize
import viewdsl.withViewBuilder

class SettingsScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootCoordinatorLayout {
      ScrollableConstraintLayout {
        paddings(top = StatusBarHeight + MarginMedium)
        ImageView(WrapContent, WrapContent, style = Styles.ImageBack) {
          id(ImageBack)
          margins(start = MarginSmall)
          onClick { store.tryDispatch(OnBackPressed) }
          constraints {
            topToTopOf(parent)
            startToStartOf(parent)
          }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          id(Title)
          text(R.string.text_settings)
          margins(start = MarginNormal)
          textSize(TextSizes.H1)
          constraints {
            startToEndOf(ImageBack)
            topToTopOf(ImageBack)
            bottomToBottomOf(ImageBack)
          }
        }
        VerticalLayout(MatchParent, WrapContent) {
          id(LayoutSettingsItems)
          margins(top = (GradientDrawableHeight / 3.5).toInt())
          constraints {
            topToBottomOf(Title)
          }
          View(MatchParent, IntSize(DividerHeight)) {
            backgroundColor(Colors.Divider)
          }
          SettingsItem(
            id = ItemChangePassword,
            title = R.string.text_change_master_password,
            description = R.string.text_change_master_password_description,
            clickable = true,
            onClick = { store.tryDispatch(OnChangeMasterPasswordClicked) }
          )
          View(MatchParent, IntSize(DividerHeight)) {
            backgroundColor(Colors.Divider)
          }
          SettingsItem(
            id = ItemShowUsernames,
            title = R.string.text_show_passwords_usernames,
            description = R.string.text_show_password_usernames_description,
            switchEnabled = true,
            onSwitchChecked = onShowUsernamesChanged,
          )
          View(MatchParent, IntSize(DividerHeight)) {
            backgroundColor(Colors.Divider)
          }
          SettingsItem(
            id = ItemBiometrics,
            title = R.string.text_biometrics_title,
            description = R.string.text_biometrics_description,
            switchEnabled = true,
            onSwitchChecked = onEnableBiometricsChanged,
          )
          View(MatchParent, IntSize(DividerHeight)) {
            id(FourthDivider)
            backgroundColor(Colors.Divider)
          }
          SettingsItem(
            id = ItemStorageBackup,
            title = R.string.text_storage_backup_title,
            description = R.string.text_storage_backup_description,
            switchEnabled = true,
            onSwitchChecked = onEnableStorageBackupChanged,
          )
          SettingsItem(
            id = ItemStorageBackupFolder,
            title = R.string.text_storage_backup_folder_title,
            description = R.string.text_storage_backup_folder_none_selected,
            clickable = true,
            onClick = { store.tryDispatch(OnSelectBackupFolderClicked) },
          )
          SettingsItem(
            id = ItemStorageBackupNow,
            title = R.string.text_storage_backup_backup_now,
            description = R.string.text_storage_backup_last_backup_never,
            clickable = true,
            onClick = { store.tryDispatch(OnBackupNowClicked) },
          )
          View(MatchParent, IntSize(DividerHeight)) {
            backgroundColor(Colors.Divider)
          }
          SettingsItem(
            id = ItemClearImagesCache,
            title = R.string.text_clear_images_cache,
            description = R.string.text_clear_images_cache_description,
            clickable = true,
            onClick = { store.tryDispatch(OnClearImagesCacheClicked) },
          )
          View(MatchParent, IntSize(DividerHeight)) {
            backgroundColor(Colors.Divider)
          }
        }
      }
      InfoDialog()
      Snackbar {
        layoutGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
        margin(MarginNormal)
      }
      EnterPasswordDialog(
        mode = CheckingMasterPassword,
        onDialogClosed = { store.tryDispatch(OnHideEnterPasswordDialog) },
        onCheckSuccessful = { store.tryDispatch(OnEnteredPasswordToChangeMasterPassword) },
      )
      LoadingDialog()
    }
  }
  
  private val onShowUsernamesChanged: (Boolean) -> Unit = {
    store.tryDispatch(OnShowUsernamesChanged(it))
  }
  
  private val onEnableBiometricsChanged: (Boolean) -> Unit = {
    store.tryDispatch(OnEnableBiometricsChanged(it))
  }
  
  private val onEnableStorageBackupChanged: (Boolean) -> Unit = {
    store.tryDispatch(OnEnableStorageBackupChanged(it))
  }
  
  private val selectFolderResultLauncher = coreComponent.activityResultWrapper
      .wrapSelectFolderLauncher(this) { uri ->
        context?.contentResolver?.takePersistableUriPermission(
          uri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        store.tryDispatch(OnSelectedBackupFolder(uri))
      }
  
  private val biometricsDialog by lazy {
    BiometricsDialog.create(this, R.string.text_biometrics_set_up)
  }
  
  private val store by viewModelStore { SettingsStore(coreComponent) }
  
  override fun onInit() {
    store.subscribe(this, ::render, ::handleNews)
    store.tryDispatch(OnInit)
    coreComponent.changeMasterPasswordObserver.masterPasswordChanges
        .onEach { store.tryDispatch(OnMasterPasswordChangedReceived) }
        .launchIn(lifecycleScope)
    initBiometricsDialog()
  }
  
  @OptIn(FlowPreview::class)
  private fun initBiometricsDialog() {
    biometricsDialog.events
        .onEach { event -> store.tryDispatch(OnBiometricsEvent(event)) }
        .launchIn(lifecycleScope)
    biometricsDialog.openedStatus
        // In case dialog opens and closes very fast due to existing error
        .debounce(100L)
        .onEach { opened -> setStatusBarColor(if (opened) Colors.Black else Colors.Transparent) }
        .launchIn(lifecycleScope)
  }
  
  override fun onAppearedOnScreen() {
    // If user clicked on switch enabling storage backup, but hasn't actually select folder,
    // we should re-request fetching storage backup preferences and disable switch if necessary
    store.tryDispatch(OnAppearedOnScreen)
  }
  
  private fun render(state: SettingsState) {
    view(ItemBiometrics).isVisible = state.biometricsAvailable
    view(FourthDivider).isVisible = state.biometricsAvailable
    when (state.enterPasswordDialogState) {
      SHOWN -> enterPasswordDialog.show()
      HIDDEN -> enterPasswordDialog.hide()
      HIDDEN_KEEPING_KEYBOARD -> enterPasswordDialog.hide(hideKeyboard = false)
    }
    if (state.showEnableBiometricsDialog) {
      infoDialog.showWithCancelAndProceedOption(
        titleRes = R.string.text_master_password_changed_biometrics_dialog_title,
        message = getText(R.string.text_master_password_changed_biometrics_dialog_message),
        proceedTextRes = R.string.text_biometrics_enable,
        showProceedAsError = false,
        onProceed = { store.tryDispatch(OnProceedEnableBiometricsDialog) },
        onCancel = { store.tryDispatch(OnHideEnableBiometricsDialog) },
      )
    } else {
      infoDialog.hide()
    }
    viewAs<SettingsItem>(ItemStorageBackupFolder).apply {
      isEnabled = state.storageBackupEnabled
      val text = state.storageBackupFolderUri?.toReadablePath()
          ?: getText(R.string.text_storage_backup_folder_none_selected)
      setDescription(text)
    }
    viewAs<SettingsItem>(ItemStorageBackupNow).apply {
      isEnabled = state.storageBackupEnabled
      val text = if (state.storageBackupLatestDate != null) {
        getString(R.string.text_storage_backup_latest_backup_prefix, state.storageBackupLatestDate)
      } else {
        getString(R.string.text_storage_backup_last_backup_never)
      }
      setDescription(text)
    }
    if (state.showLoadingBackingUp) {
      loadingDialog.show()
    } else {
      loadingDialog.hide()
    }
  }
  
  private fun handleNews(news: SettingsNews) {
    when (news) {
      is SetShowUsernames -> {
        viewAs<SettingsItem>(ItemShowUsernames).setCheckedSilently(
          checked = news.showUsernames,
          animate = false,
          onChecked = onShowUsernamesChanged
        )
      }
      is SetBiometricsEnabled -> {
        viewAs<SettingsItem>(ItemBiometrics).setCheckedSilently(
          checked = news.enabled,
          animate = news.animate,
          onChecked = onEnableBiometricsChanged
        )
      }
      is SetStorageBackupEnabled -> {
        viewAs<SettingsItem>(ItemStorageBackup).setCheckedSilently(
          checked = news.enabled,
          animate = false,
          onChecked = onEnableStorageBackupChanged
        )
      }
      ShowMasterPasswordChanged -> {
        lifecycleScope.launch {
          delay(Durations.Default)
          snackbar.show(CHECKMARK, R.string.text_master_password_changed)
        }
      }
      ShowBiometricsPrompt -> {
        val cipherProvider = coreComponent.biometricsCipherProvider
        biometricsDialog.launch(cipherProvider.getCipherForEncryption())
      }
      ShowBiometricsEnabled -> {
        snackbar.show(CHECKMARK, R.string.text_biometrics_enabled)
      }
      is ShowBiometricsError -> {
        val textRes = when (news.error) {
          LOCKOUT -> R.string.text_biometrics_snackbar_error_lockout
          LOCKOUT_PERMANENT -> R.string.text_biometrics_snackbar_error_lockout_permanent
          OTHER -> R.string.text_biometrics_snackbar_error
        }
        snackbar.show(ERROR, textRes)
      }
      is LaunchFolderSelection -> {
        selectFolderResultLauncher.launch(news.initialUri)
      }
      ShowStorageBackupEnabled -> {
        snackbar.show(CHECKMARK, R.string.text_storage_backup_enabled)
      }
      ShowBackupPerformed -> {
        snackbar.show(CHECKMARK, R.string.text_storage_backup_performed)
      }
      ShowImagesCacheCleared -> {
        snackbar.show(CHECKMARK, R.string.text_clear_images_cache_cleared)
      }
    }
  }
  
  override fun handleBackPress(): Boolean {
    store.tryDispatch(OnBackPressed)
    return true
  }
  
  companion object {
    
    val ImageBack = View.generateViewId()
    val Title = View.generateViewId()
    val LayoutSettingsItems = View.generateViewId()
    val ItemChangePassword = View.generateViewId()
    val ItemShowUsernames = View.generateViewId()
    val ItemBiometrics = View.generateViewId()
    val FourthDivider = View.generateViewId()
    val ItemStorageBackup = View.generateViewId()
    val ItemStorageBackupFolder = View.generateViewId()
    val ItemStorageBackupNow = View.generateViewId()
    val ItemClearImagesCache = View.generateViewId()
  }
}
