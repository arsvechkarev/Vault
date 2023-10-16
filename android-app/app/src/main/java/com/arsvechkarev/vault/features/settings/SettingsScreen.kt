package com.arsvechkarev.vault.features.settings

import android.content.Context
import android.view.Gravity
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.arsvechkarev.vault.R
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
import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.HIDDEN
import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.HIDDEN_KEEPING_KEYBOARD
import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.SHOWN
import com.arsvechkarev.vault.features.settings.SettingsBiometricsError.LOCKOUT
import com.arsvechkarev.vault.features.settings.SettingsBiometricsError.LOCKOUT_PERMANENT
import com.arsvechkarev.vault.features.settings.SettingsBiometricsError.OTHER
import com.arsvechkarev.vault.features.settings.SettingsNews.SetBiometricsEnabled
import com.arsvechkarev.vault.features.settings.SettingsNews.SetShowUsernames
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowBiometricsAdded
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowBiometricsError
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowBiometricsPrompt
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowImagesCacheCleared
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowMasterPasswordChanged
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnBiometricsEvent
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnChangeMasterPasswordClicked
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnClearImagesCacheClicked
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnEnableBiometricsChanged
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnEnteredPasswordToChangeMasterPassword
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnHideEnterPasswordDialog
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnInit
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
import kotlinx.coroutines.delay
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
      child<ConstraintLayout>(MatchParent, WrapContent) {
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
          margins(top = GradientDrawableHeight / 3)
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
      Snackbar {
        layoutGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
        margin(MarginNormal)
      }
      EnterPasswordDialog(
        mode = CheckingMasterPassword,
        onDialogClosed = { store.tryDispatch(OnHideEnterPasswordDialog) },
        onCheckSuccessful = { store.tryDispatch(OnEnteredPasswordToChangeMasterPassword) },
      )
    }
  }
  
  private val onShowUsernamesChanged: (Boolean) -> Unit = {
    store.tryDispatch(OnShowUsernamesChanged(it))
  }
  
  private val onEnableBiometricsChanged: (Boolean) -> Unit = {
    store.tryDispatch(OnEnableBiometricsChanged(it))
  }
  
  private val biometricsDialog by lazy {
    BiometricsDialog.create(this, R.string.text_biometrics_add_fingerprint)
  }
  
  private val store by viewModelStore { SettingsStore(coreComponent) }
  
  override fun onInit() {
    store.subscribe(this, ::render, ::handleNews)
    store.tryDispatch(OnInit)
    biometricsDialog.events
        .onEach { event -> store.tryDispatch(OnBiometricsEvent(event)) }
        .launchIn(lifecycleScope)
  }
  
  private fun render(state: SettingsState) {
    view(ItemBiometrics).isVisible = state.biometricsAvailable
    view(FourthDivider).isVisible = state.biometricsAvailable
    when (state.enterPasswordDialogState) {
      SHOWN -> enterPasswordDialog.show()
      HIDDEN -> enterPasswordDialog.hide()
      HIDDEN_KEEPING_KEYBOARD -> enterPasswordDialog.hide(hideKeyboard = false)
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
      ShowBiometricsAdded -> {
        snackbar.show(CHECKMARK, R.string.text_biometrics_added)
      }
      is ShowBiometricsError -> {
        val textRes = when (news.error) {
          LOCKOUT -> R.string.text_biometrics_snackbar_error_lockout
          LOCKOUT_PERMANENT -> R.string.text_biometrics_snackbar_error_lockout_permanent
          OTHER -> R.string.text_biometrics_snackbar_error
        }
        snackbar.show(ERROR, textRes)
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
    val ItemClearImagesCache = View.generateViewId()
  }
}
