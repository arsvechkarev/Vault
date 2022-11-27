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
import com.arsvechkarev.vault.core.views.EditTextPassword
import com.arsvechkarev.vault.core.views.MaterialProgressBar
import com.arsvechkarev.vault.core.views.MaterialProgressBar.Thickness
import com.arsvechkarev.vault.core.views.behaviors.BottomSheetBehavior
import com.arsvechkarev.vault.core.views.behaviors.BottomSheetBehavior.Companion.asBottomSheet
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Mode.CHECK_MASTER_PASSWORD
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Mode.IMPORTING_PASSWORDS
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.IconSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginExtraLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginTiny
import com.arsvechkarev.vault.viewbuilding.Dimens.ProgressBarSizeSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.Styles.ImageCross
import com.arsvechkarev.vault.viewbuilding.TextSizes
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
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.textView
import viewdsl.viewAs
import viewdsl.withViewBuilder

class EnterPasswordDialog(context: Context) : FrameLayout(context) {
  
  private var mode: Mode? = null
  
  private var onCheckSuccessful: () -> Unit = {}
  private var onPasswordEntered: (String) -> Unit = {}
  
  val isDialogShown get() = asBottomSheet.isShown
  
  init {
    withViewBuilder {
      backgroundRoundTopRect(MarginNormal, Colors.Dialog)
      VerticalLayout(MatchParent, WrapContent) {
        id(BottomSheetLayout)
        padding(MarginNormal)
        layoutGravity(BOTTOM)
        child<FrameLayout>(MatchParent, MatchParent) {
          TextView(WrapContent, WrapContent, style = BoldTextView) {
            id(Title)
            textSize(TextSizes.H2)
            margins(end = IconSize * 2)
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
          text("qwetu1233") // TODO (27.11.2022): REMOVE THIS!
          setHint(R.string.hint_enter_password)
          onTextChanged { parentView.textView(TextError).clearText() }
          onSubmit { password -> handleContinueClick(password) }
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
            text(R.string.text_continue)
            onClick {
              val editTextPassword = parentView.parentView.viewAs<EditTextPassword>()
              val text = editTextPassword.getText()
              if (text.isNotBlank()) {
                handleContinueClick(text)
              }
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
  }
  
  private fun setupMode(
    mode: Mode,
    onCheckSuccessful: () -> Unit,
    onPasswordEntered: (String) -> Unit
  ) {
    this.mode = mode
    this.onCheckSuccessful = onCheckSuccessful
    this.onPasswordEntered = onPasswordEntered
    when (mode) {
      IMPORTING_PASSWORDS -> {
        textView(Title).text(R.string.text_enter_password_to_import)
      }
      CHECK_MASTER_PASSWORD -> {
        textView(Title).text(R.string.text_enter_master_password_to_proceed)
      }
    }
  }
  
  private fun ViewBuilder.handleContinueClick(text: String) {
    when (checkNotNull(mode)) {
      IMPORTING_PASSWORDS -> onPasswordEntered(text)
      CHECK_MASTER_PASSWORD -> checkMasterPassword(text)
    }
  }
  
  private fun ViewBuilder.checkMasterPassword(password: String) {
    (context as LifecycleOwner).lifecycleScope.launch {
      val passwordChecker = coreComponent.masterPasswordChecker
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
  
  enum class Mode {
    IMPORTING_PASSWORDS, CHECK_MASTER_PASSWORD
  }
  
  companion object {
    
    private val ShadowLayout = View.generateViewId()
    private val Title = View.generateViewId()
    private val BottomSheetLayout = View.generateViewId()
    private val TextError = View.generateViewId()
    private val ButtonCheck = View.generateViewId()
    
    val BaseFragmentScreen.enterPasswordDialog get() = viewAs<EnterPasswordDialog>()
    
    fun CoordinatorLayout.EnterPasswordDialog(
      mode: Mode,
      onCheckSuccessful: () -> Unit = {},
      onPasswordEntered: (String) -> Unit = {},
      onDialogClosed: () -> Unit = {},
      block: EnterPasswordDialog.() -> Unit = {}
    ) = withViewBuilder {
      val shadowLayout = child<FrameLayout>(MatchParent, MatchParent) {
        id(ShadowLayout)
      }
      child<EnterPasswordDialog, ViewGroup.LayoutParams>(MatchParent, WrapContent, block) {
        classNameTag()
        setupBehavior(shadowLayout, onDialogClosed)
        setupMode(mode, onCheckSuccessful, onPasswordEntered)
      }
    }
  }
}
