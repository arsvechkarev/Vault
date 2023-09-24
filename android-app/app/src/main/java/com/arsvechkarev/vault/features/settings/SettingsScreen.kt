package com.arsvechkarev.vault.features.settings

import android.content.Context
import android.view.Gravity
import android.view.View
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
import com.arsvechkarev.vault.features.settings.SettingsNews.ShowMasterPasswordChanged
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnChangeMasterPasswordClicked
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnEnteredPasswordToChangeMasterPassword
import com.arsvechkarev.vault.features.settings.SettingsUiEvent.OnHideEnterPasswordDialog
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.DividerHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.GradientDrawableHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.AccentTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ImageBack
import com.arsvechkarev.vault.viewbuilding.Styles.SecondaryTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.IntSize
import viewdsl.backgroundColor
import viewdsl.classNameTag
import viewdsl.constraints
import viewdsl.gravity
import viewdsl.id
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.margins
import viewdsl.onClick
import viewdsl.padding
import viewdsl.rippleBackground
import viewdsl.text
import viewdsl.textSize
import viewdsl.withViewBuilder

class SettingsScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootCoordinatorLayout {
      child<ConstraintLayout>(MatchParent, WrapContent) {
        HorizontalLayout(MatchParent, WrapContent) {
          id(Toolbar)
          margins(top = StatusBarHeight)
          constraints {
            topToTopOf(parent)
          }
          ImageView(WrapContent, WrapContent, style = ImageBack) {
            margin(MarginNormal)
            gravity(Gravity.CENTER_VERTICAL)
            onClick { store.tryDispatch(OnBackPressed) }
          }
          TextView(WrapContent, WrapContent, style = BoldTextView) {
            layoutGravity(Gravity.CENTER)
            text(R.string.text_settings)
            textSize(TextSizes.H1)
          }
        }
        View(MatchParent, IntSize(DividerHeight)) {
          id(FirstDivider)
          margins(top = GradientDrawableHeight)
          backgroundColor(Colors.Divider)
          constraints {
            topToTopOf(Toolbar)
          }
        }
        VerticalLayout(MatchParent, WrapContent) {
          id(ItemChangePassword)
          padding(MarginNormal)
          rippleBackground(Colors.Ripple)
          onClick { store.tryDispatch(OnChangeMasterPasswordClicked) }
          constraints {
            topToBottomOf(FirstDivider)
          }
          TextView(WrapContent, WrapContent, style = AccentTextView) {
            text(R.string.text_change_master_password)
          }
          TextView(WrapContent, WrapContent, style = SecondaryTextView) {
            text(R.string.text_change_master_password_description)
            textSize(TextSizes.H4)
            margins(top = MarginSmall)
          }
        }
        View(MatchParent, IntSize(DividerHeight)) {
          id(SecondDivider)
          backgroundColor(Colors.Divider)
          constraints {
            topToBottomOf(ItemChangePassword)
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
  
  private val store by viewModelStore { SettingsStore(coreComponent) }
  
  override fun onInit() {
    store.subscribe(this, ::render, ::handleNews)
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
    
    val Toolbar = View.generateViewId()
    val FirstDivider = View.generateViewId()
    val ItemChangePassword = View.generateViewId()
    val SecondDivider = View.generateViewId()
  }
}
