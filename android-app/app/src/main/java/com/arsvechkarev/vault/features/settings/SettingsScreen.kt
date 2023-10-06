package com.arsvechkarev.vault.features.settings

import android.content.Context
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.core.views.Snackbar
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Companion.EnterPasswordDialog
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Companion.enterPasswordDialog
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Mode.CheckingMasterPassword
import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.HIDDEN
import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.HIDDEN_KEEPING_KEYBOARD
import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.SHOWN
import com.arsvechkarev.vault.features.settings.SettingsNews.SetShowUsernames
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowImagesCacheCleared
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowMasterPasswordChanged
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.FetchShowUsernamesChecked
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnChangeMasterPasswordClicked
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnClearImagesCacheClicked
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnEnteredPasswordToChangeMasterPassword
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnHideEnterPasswordDialog
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnShowUsernamesChanged
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.DividerHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.GradientDrawableHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginMedium
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles
import com.arsvechkarev.vault.viewbuilding.Styles.AccentTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.SecondaryTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Switch
import com.arsvechkarev.vault.viewbuilding.TextSizes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.Companion.ZERO
import viewdsl.Size.IntSize
import viewdsl.backgroundColor
import viewdsl.classNameTag
import viewdsl.constraints
import viewdsl.id
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.margins
import viewdsl.onClick
import viewdsl.paddings
import viewdsl.rippleBackground
import viewdsl.setCheckedSilently
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
        View(MatchParent, IntSize(DividerHeight)) {
          id(FirstDivider)
          margins(top = GradientDrawableHeight / 3 - MarginNormal)
          backgroundColor(Colors.Divider)
          constraints {
            topToBottomOf(Title)
          }
        }
        View(MatchParent, ZERO) {
          id(ItemChangePassword)
          rippleBackground(Colors.Ripple)
          onClick { store.tryDispatch(OnChangeMasterPasswordClicked) }
          constraints {
            topToBottomOf(FirstDivider)
            bottomToTopOf(SecondDivider)
          }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TitleChangeMasterPassword)
          text(R.string.text_change_master_password)
          textSize(TextSizes.H5)
          margins(start = MarginNormal, top = MarginNormal)
          constraints {
            startToStartOf(parent)
            topToBottomOf(FirstDivider)
          }
        }
        TextView(ZERO, WrapContent, style = SecondaryTextView) {
          id(TextChangeMasterPassword)
          text(R.string.text_change_master_password_description)
          margins(start = MarginNormal, top = MarginSmall, end = MarginNormal)
          constraints {
            startToStartOf(parent)
            endToEndOf(parent)
            topToBottomOf(TitleChangeMasterPassword)
          }
        }
        View(MatchParent, IntSize(DividerHeight)) {
          id(SecondDivider)
          backgroundColor(Colors.Divider)
          margins(top = MarginNormal)
          constraints {
            topToBottomOf(TextChangeMasterPassword)
          }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TitleShowUsernames)
          text(R.string.text_show_passwords_usernames)
          textSize(TextSizes.H5)
          margins(start = MarginNormal, top = MarginNormal)
          constraints {
            startToStartOf(parent)
            topToBottomOf(SecondDivider)
          }
        }
        TextView(ZERO, WrapContent, style = SecondaryTextView) {
          id(TextShowUsernames)
          text(R.string.text_show_password_usernames_description)
          margins(start = MarginNormal, top = MarginSmall)
          constraints {
            startToStartOf(parent)
            endToStartOf(SwitchShowUsernames)
            topToBottomOf(TitleShowUsernames)
          }
        }
        child<SwitchCompat>(WrapContent, WrapContent, style = Switch) {
          id(SwitchShowUsernames)
          margin(MarginSmall)
          constraints {
            endToEndOf(parent)
            topToBottomOf(SecondDivider)
            bottomToTopOf(ThirdDivider)
          }
        }
        View(MatchParent, IntSize(DividerHeight)) {
          id(ThirdDivider)
          backgroundColor(Colors.Divider)
          margins(top = MarginNormal)
          constraints {
            topToBottomOf(TextShowUsernames)
          }
        }
        View(MatchParent, ZERO) {
          id(ItemClearImagesCache)
          rippleBackground(Colors.Ripple)
          onClick { store.tryDispatch(OnClearImagesCacheClicked) }
          constraints {
            topToBottomOf(ThirdDivider)
            bottomToTopOf(FourthDivider)
          }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TitleClearImagesCache)
          text(R.string.text_clear_images_cache)
          textSize(TextSizes.H5)
          margins(start = MarginNormal, top = MarginNormal)
          constraints {
            startToStartOf(parent)
            topToBottomOf(ThirdDivider)
          }
        }
        TextView(ZERO, WrapContent, style = SecondaryTextView) {
          id(TextClearImagesCache)
          text(R.string.text_clear_images_cache_description)
          margins(start = MarginNormal, top = MarginSmall, end = MarginNormal)
          constraints {
            startToStartOf(parent)
            endToEndOf(parent)
            topToBottomOf(TitleClearImagesCache)
          }
        }
        View(MatchParent, IntSize(DividerHeight)) {
          id(FourthDivider)
          backgroundColor(Colors.Divider)
          margins(top = MarginNormal)
          constraints {
            topToBottomOf(TextClearImagesCache)
          }
        }
      }
      child<Snackbar>(MatchParent, WrapContent) {
        classNameTag()
        layoutGravity(Gravity.BOTTOM)
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
  
  private val store by viewModelStore { SettingsStore(coreComponent) }
  
  override fun onInit() {
    store.subscribe(this, ::render, ::handleNews)
    store.tryDispatch(FetchShowUsernamesChecked)
  }
  
  private fun render(state: SettingsState) {
    when (state.enterPasswordDialogState) {
      SHOWN -> enterPasswordDialog.show()
      HIDDEN -> enterPasswordDialog.hide()
      HIDDEN_KEEPING_KEYBOARD -> enterPasswordDialog.hide(hideKeyboard = false)
    }
  }
  
  private fun handleNews(news: SettingsNews) {
    when (news) {
      is SetShowUsernames -> {
        viewAs<SwitchCompat>(SwitchShowUsernames).setCheckedSilently(news.showUsernames,
          onChecked = onShowUsernamesChanged)
      }
      ShowImagesCacheCleared -> {
        viewAs<Snackbar>().show(R.string.text_clear_images_cache_cleared)
      }
      ShowMasterPasswordChanged -> {
        lifecycleScope.launch {
          delay(Durations.Default)
          viewAs<Snackbar>().show(R.string.text_master_password_changed)
        }
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
    val FirstDivider = View.generateViewId()
    val ItemChangePassword = View.generateViewId()
    val TitleChangeMasterPassword = View.generateViewId()
    val TextChangeMasterPassword = View.generateViewId()
    val SecondDivider = View.generateViewId()
    val TitleShowUsernames = View.generateViewId()
    val TextShowUsernames = View.generateViewId()
    val SwitchShowUsernames = View.generateViewId()
    val ThirdDivider = View.generateViewId()
    val ItemClearImagesCache = View.generateViewId()
    val TitleClearImagesCache = View.generateViewId()
    val TextClearImagesCache = View.generateViewId()
    val FourthDivider = View.generateViewId()
  }
}
