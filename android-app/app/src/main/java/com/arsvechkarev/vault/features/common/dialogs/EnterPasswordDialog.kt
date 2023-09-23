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
import buisnesslogic.Password
import com.arsvechkarev.vault.BuildConfig
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.EditTextPassword
import com.arsvechkarev.vault.core.views.FixedHeightTextView
import com.arsvechkarev.vault.core.views.MaterialProgressBar
import com.arsvechkarev.vault.core.views.MaterialProgressBar.Thickness
import com.arsvechkarev.vault.core.views.behaviors.BottomSheetBehavior
import com.arsvechkarev.vault.core.views.behaviors.BottomSheetBehavior.Companion.asBottomSheet
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Mode.CHECK_MASTER_PASSWORD
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Mode.IMPORTING_PASSWORDS
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens
import com.arsvechkarev.vault.viewbuilding.Dimens.IconSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginExtraLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginTiny
import com.arsvechkarev.vault.viewbuilding.Dimens.ProgressBarSizeSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableGradientRoundRect
import com.arsvechkarev.vault.viewbuilding.Styles.IconCross
import com.arsvechkarev.vault.viewbuilding.TextSizes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.ViewBuilder
import viewdsl.backgroundTopRoundRect
import viewdsl.behavior
import viewdsl.classNameTag
import viewdsl.clearText
import viewdsl.gravity
import viewdsl.hideKeyboard
import viewdsl.id
import viewdsl.invisible
import viewdsl.isVisible
import viewdsl.layoutGravity
import viewdsl.margins
import viewdsl.onClick
import viewdsl.padding
import viewdsl.parentView
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.textView
import viewdsl.viewAs
import viewdsl.visible
import viewdsl.withViewBuilder

class EnterPasswordDialog(context: Context) : FrameLayout(context) {
  
  private var mode: Mode? = null
  
  private var hideKeyboardOnClose = true
  private var onCheckSuccessful: () -> Unit = {}
  private var onPasswordEntered: (Password) -> Unit = {}
  
  val isDialogShown get() = asBottomSheet.isShown
  
  init {
    withViewBuilder {
      backgroundTopRoundRect(MarginNormal, Colors.Dialog)
      VerticalLayout(MatchParent, WrapContent) {
        padding(MarginNormal)
        layoutGravity(BOTTOM)
        child<FrameLayout>(MatchParent, MatchParent) {
          TextView(WrapContent, WrapContent, style = BoldTextView) {
            id(Title)
            textSize(TextSizes.H2)
            margins(end = IconSize * 2)
            layoutGravity(CENTER_VERTICAL)
          }
          ImageView(WrapContent, WrapContent, style = IconCross) {
            layoutGravity(CENTER_VERTICAL or END)
            onClick { hide() }
          }
        }
        child<EditTextPassword>(MatchParent, WrapContent) {
          margins(top = MarginExtraLarge + MarginNormal)
          classNameTag()
          setHint(R.string.hint_enter_password)
          onTextChanged { parentView.textView(TextError).clearText() }
          onSubmit { password -> handleContinueClick(password) }
        }
        child<FixedHeightTextView>(MatchParent, WrapContent, style = BaseTextView) {
          id(TextError)
          textColor(Colors.Error)
          margins(start = MarginTiny, top = MarginSmall)
        }
        FrameLayout(MatchParent, WrapContent) {
          margins(top = MarginExtraLarge)
          FrameLayout(MatchParent, WrapContent, style = ClickableGradientRoundRect()) {
            id(ButtonContinue)
            onClick {
              val editTextPassword = parentView.parentView.viewAs<EditTextPassword>()
              val text = editTextPassword.getPassword()
              if (text.stringData.isNotBlank()) {
                handleContinueClick(text)
              } else {
                parentView.parentView.textView(TextError)
                    .text(R.string.text_password_cannot_be_empty)
              }
            }
            TextView(MatchParent, WrapContent, style = BoldTextView) {
              id(TextContinue)
              padding(Dimens.MarginMedium)
              textSize(TextSizes.H4)
              gravity(CENTER)
              text(R.string.text_continue)
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
  
  fun show() {
    asBottomSheet.show()
  }
  
  fun hide() {
    asBottomSheet.hide()
  }
  
  private fun ViewBuilder.setupBehavior(shadowLayout: FrameLayout, onDialogClosed: () -> Unit = {}) {
    behavior(BottomSheetBehavior().apply {
      onShow = {
        viewAs<EditTextPassword>().showKeyboard()
        if (BuildConfig.DEBUG) {
          viewAs<EditTextPassword>().setRawText(BuildConfig.STUB_PASSWORD)
        }
      }
      onHide = {
        viewAs<EditTextPassword>().clearTextAndFocus()
        if (hideKeyboardOnClose) {
          context.hideKeyboard()
        }
        onDialogClosed()
      }
      onSlidePercentageChanged = { fraction ->
        val color = ColorUtils.blendARGB(Color.TRANSPARENT, Colors.Shadow, fraction)
        shadowLayout.setBackgroundColor(color)
      }
    })
  }
  
  private fun setupMode(
    mode: Mode,
    hideKeyboardOnClose: Boolean,
    onCheckSuccessful: () -> Unit,
    onPasswordEntered: (Password) -> Unit
  ) {
    this.mode = mode
    this.onCheckSuccessful = onCheckSuccessful
    this.onPasswordEntered = onPasswordEntered
    this.hideKeyboardOnClose = hideKeyboardOnClose
    when (mode) {
      IMPORTING_PASSWORDS -> {
        textView(Title).text(R.string.text_enter_password_to_import)
      }
      
      CHECK_MASTER_PASSWORD -> {
        textView(Title).text(R.string.text_enter_master_password_to_proceed)
      }
    }
  }
  
  private fun ViewBuilder.handleContinueClick(text: Password) {
    when (checkNotNull(mode)) {
      IMPORTING_PASSWORDS -> onPasswordEntered(text)
      CHECK_MASTER_PASSWORD -> checkMasterPassword(text)
    }
  }
  
  private fun ViewBuilder.checkMasterPassword(password: Password) {
    (context as LifecycleOwner).lifecycleScope.launch {
      val passwordChecker = coreComponent.masterPasswordChecker
      viewAs<MaterialProgressBar>().isVisible = true
      textView(TextContinue).invisible()
      delay(500)
      if (passwordChecker.isCorrect(password)) {
        onCheckSuccessful()
      } else {
        textView(TextError).text(R.string.text_password_is_incorrect)
      }
      textView(TextContinue).visible()
      viewAs<MaterialProgressBar>().isVisible = false
    }
  }
  
  enum class Mode {
    IMPORTING_PASSWORDS, CHECK_MASTER_PASSWORD
  }
  
  companion object {
    
    val Title = View.generateViewId()
    val TextError = View.generateViewId()
    val ButtonContinue = View.generateViewId()
    val TextContinue = View.generateViewId()
    
    val BaseFragmentScreen.enterPasswordDialog get() = viewAs<EnterPasswordDialog>()
    
    fun CoordinatorLayout.EnterPasswordDialog(
      mode: Mode,
      hideKeyboardOnClose: Boolean = true,
      onCheckSuccessful: () -> Unit = {},
      onPasswordEntered: (Password) -> Unit = {},
      onDialogClosed: () -> Unit = {},
      block: EnterPasswordDialog.() -> Unit = {}
    ) = withViewBuilder {
      val shadowLayout = child<FrameLayout>(MatchParent, MatchParent)
      child<EnterPasswordDialog, ViewGroup.LayoutParams>(MatchParent, WrapContent, block) {
        classNameTag()
        setupBehavior(shadowLayout, onDialogClosed)
        setupMode(mode, hideKeyboardOnClose, onCheckSuccessful, onPasswordEntered)
      }
    }
  }
}
