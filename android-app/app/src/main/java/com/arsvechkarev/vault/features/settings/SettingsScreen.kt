package com.arsvechkarev.vault.features.settings

import android.content.Context
import android.view.Gravity
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.common.Screens.ChangeMasterPasswordScreen
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Companion.EnterPasswordDialog
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Companion.enterPasswordDialog
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Mode.CHECK_MASTER_PASSWORD
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.DividerHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.GradientDrawableHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.AccentTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.IconBack
import com.arsvechkarev.vault.viewbuilding.Styles.SecondaryTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.IntSize
import viewdsl.backgroundColor
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
          ImageView(WrapContent, WrapContent, style = IconBack) {
            margin(MarginNormal)
            gravity(Gravity.CENTER_VERTICAL)
            onClick { handleBackPress() }
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
          onClick { enterPasswordDialog.show() }
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
      EnterPasswordDialog(
        mode = CHECK_MASTER_PASSWORD,
        hideKeyboardOnClose = false,
        onCheckSuccessful = {
          coreComponent.router.goForward(ChangeMasterPasswordScreen)
          enterPasswordDialog.hide()
        },
      )
    }
  }
  
  override fun handleBackPress(): Boolean {
    if (enterPasswordDialog.isDialogShown) {
      enterPasswordDialog.hide()
    } else {
      coreComponent.router.goBack()
    }
    return true
  }
  
  private companion object {
    
    val Toolbar = View.generateViewId()
    val FirstDivider = View.generateViewId()
    val ItemChangePassword = View.generateViewId()
    val SecondDivider = View.generateViewId()
  }
}
