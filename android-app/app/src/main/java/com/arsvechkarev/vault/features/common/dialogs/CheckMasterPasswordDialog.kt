package com.arsvechkarev.vault.features.common.dialogs

import android.content.Context
import android.graphics.Color
import android.view.Gravity.BOTTOM
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_VERTICAL
import android.view.Gravity.END
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.di.AppComponentProvider
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginExtraLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginTiny
import com.arsvechkarev.vault.viewbuilding.Dimens.ProgressBarSizeSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.Styles.ImageCross
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.views.EditTextPassword
import com.arsvechkarev.vault.views.MaterialProgressBar
import com.arsvechkarev.vault.views.MaterialProgressBar.Thickness
import com.arsvechkarev.vault.views.behaviors.BottomSheetBehavior
import com.arsvechkarev.vault.views.behaviors.BottomSheetBehavior.Companion.asBottomSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.ViewBuilder
import viewdsl.backgroundRoundTopRect
import viewdsl.behavior
import viewdsl.classNameTag
import viewdsl.clearText
import viewdsl.hideKeyboard
import viewdsl.id
import viewdsl.invisible
import viewdsl.isVisible
import viewdsl.layoutGravity
import viewdsl.margins
import viewdsl.onClick
import viewdsl.padding
import viewdsl.parentView
import viewdsl.showKeyboard
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.textView
import viewdsl.viewAs
import viewdsl.withViewBuilder

class CheckMasterPasswordDialog(context: Context) : FrameLayout(context) {
  
  private var onCheckSuccessful: () -> Unit = {}
  
  init {
    withViewBuilder {
      backgroundRoundTopRect(MarginNormal, Colors.Dialog)
      VerticalLayout(MatchParent, WrapContent) {
        id(BottomSheetLayout)
        padding(MarginNormal)
        layoutGravity(BOTTOM)
        child<FrameLayout>(MatchParent, MatchParent) {
          TextView(WrapContent, WrapContent, style = BoldTextView) {
            text(R.string.text_enter_master_password_to_proceed)
            textSize(TextSizes.H1)
            layoutGravity(CENTER_VERTICAL)
          }
          ImageView(WrapContent, WrapContent, style = ImageCross) {
            layoutGravity(CENTER_VERTICAL or END)
            onClick { hide() }
          }
        }
        child<EditTextPassword>(MatchParent, WrapContent) {
          margins(top = MarginExtraLarge + MarginNormal)
          classNameTag()
          setHint(R.string.hint_enter_password)
          onTextChanged { parentView.textView(TextError).clearText() }
          onSubmit { password -> checkMasterPassword(password) }
        }
        TextView(MatchParent, WrapContent) {
          id(TextError)
          textColor(Colors.Error)
          margins(start = MarginTiny, top = MarginSmall)
        }
        FrameLayout(MatchParent, WrapContent) {
          margins(top = MarginExtraLarge)
          TextView(MatchParent, WrapContent, style = Button()) {
            id(ButtonCheck)
            text(R.string.text_check)
            onClick {
              val editTextPassword = parentView.parentView.viewAs<EditTextPassword>()
              checkMasterPassword(editTextPassword.getText())
            }
          }
          child<MaterialProgressBar>(ProgressBarSizeSmall, ProgressBarSizeSmall) {
            layoutGravity(CENTER)
            classNameTag()
            invisible()
            setup(Colors.TextPrimary, Thickness.THICK)
          }
        }
      }
    }
  }
  
  val isDialogShown get() = asBottomSheet.isShown
  
  fun show() {
    asBottomSheet.show()
  }
  
  fun hide() {
    asBottomSheet.hide()
  }
  
  private fun ViewBuilder.checkMasterPassword(password: String) {
    (context as LifecycleOwner).lifecycleScope.launch {
      val appComponent = (context as AppComponentProvider).appComponent
      val passwordChecker = appComponent.masterPasswordChecker
      viewAs<MaterialProgressBar>().isVisible = true
      textView(ButtonCheck).clearText()
      delay(500)
      if (passwordChecker.isCorrect(password)) {
        onCheckSuccessful()
      } else {
        textView(TextError).text(R.string.text_password_is_incorrect)
      }
      textView(ButtonCheck).text(R.string.text_check)
      viewAs<MaterialProgressBar>().isVisible = false
    }
  }
  
  companion object {
  
    private val ShadowLayout = View.generateViewId()
    private val BottomSheetLayout = View.generateViewId()
    private val TextError = View.generateViewId()
    private val ButtonCheck = View.generateViewId()
  
    val BaseFragmentScreen.checkMasterPasswordDialog get() = viewAs<CheckMasterPasswordDialog>()
  
    fun CoordinatorLayout.CheckMasterPasswordDialog(
      onCheckSuccessful: () -> Unit = {},
      onDialogClosed: () -> Unit = {},
      block: CheckMasterPasswordDialog.() -> Unit = {}
    ) = withViewBuilder {
      val shadowLayout = child<FrameLayout>(MatchParent, MatchParent) {
        id(ShadowLayout)
      }
      child<CheckMasterPasswordDialog, ViewGroup.LayoutParams>(MatchParent, WrapContent, block) {
        classNameTag()
        behavior(BottomSheetBehavior().apply {
          onShow = {
            viewAs<EditTextPassword>().requestEditTextFocus()
            context.showKeyboard(viewAs<EditTextPassword>().editText)
          }
          onHide = {
            viewAs<EditTextPassword>().clear()
            context.hideKeyboard()
            onDialogClosed()
          }
          onSlidePercentageChanged = { fraction ->
            val color = ColorUtils.blendARGB(Color.TRANSPARENT, Colors.Shadow, fraction)
            shadowLayout.setBackgroundColor(color)
          }
        })
        this.onCheckSuccessful = onCheckSuccessful
      }
    }
  }
}
