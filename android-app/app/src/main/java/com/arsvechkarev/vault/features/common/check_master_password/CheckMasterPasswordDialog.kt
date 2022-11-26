package com.arsvechkarev.vault.features.common.check_master_password

import android.content.Context
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_VERTICAL
import android.view.Gravity.END
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.di.AppComponentProvider
import com.arsvechkarev.vault.features.common.dialogs.PasswordStrengthDialog
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginExtraLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.ProgressBarSizeSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.Styles.ImageCross
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.views.EditTextPassword
import com.arsvechkarev.vault.views.MaterialProgressBar
import kotlinx.coroutines.launch
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.ViewBuilder
import viewdsl.backgroundRoundTopRect
import viewdsl.classNameTag
import viewdsl.clearText
import viewdsl.gravity
import viewdsl.invisible
import viewdsl.isVisible
import viewdsl.layoutGravity
import viewdsl.margins
import viewdsl.onClick
import viewdsl.padding
import viewdsl.size
import viewdsl.tag
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.textView
import viewdsl.viewAs
import viewdsl.withViewBuilder

class CheckMasterPasswordDialog(context: Context) : FrameLayout(context) {
  
  private var onCheckSuccessful: () -> Unit = {}
  private var onDialogClosed: () -> Unit = {}
  
  init {
    withViewBuilder {
      backgroundRoundTopRect(MarginNormal, Colors.Dialog)
      VerticalLayout(MatchParent, WrapContent) {
        padding(MarginNormal)
        FrameLayout(MatchParent, MatchParent) {
          TextView(WrapContent, WrapContent, style = BoldTextView) {
            text(R.string.text_enter_master_password)
            textSize(TextSizes.H1)
            layoutGravity(CENTER_VERTICAL)
          }
          ImageView(WrapContent, WrapContent, style = ImageCross) {
            layoutGravity(CENTER_VERTICAL or END)
          }
        }
        child<EditTextPassword>(MatchParent, WrapContent) {
          margins(top = MarginExtraLarge)
          classNameTag()
          setHint(R.string.hint_enter_password)
          onTextChanged { textView(TextError).clearText() }
          onSubmit { password -> checkMasterPassword(password) }
        }
        TextView(WrapContent, WrapContent) {
          tag(TextError)
          textColor(Colors.Error)
          margins(top = MarginNormal)
        }
        FrameLayout(MatchParent, WrapContent) {
          gravity(CENTER)
          TextView(MatchParent, WrapContent, style = Button()) {
            tag(ButtonCheck)
            margins(top = MarginExtraLarge)
            text(R.string.text_check)
            onClick { checkMasterPassword(viewAs<EditTextPassword>().getText()) }
          }
          MaterialProgressBar(context).apply {
            size(ProgressBarSizeSmall, ProgressBarSizeSmall)
            classNameTag()
            invisible()
          }
        }
      }
    }
  }
  
  private fun ViewBuilder.checkMasterPassword(password: String) {
    (context as LifecycleOwner).lifecycleScope.launch {
      val appComponent = (context as AppComponentProvider).appComponent
      val passwordChecker = appComponent.masterPasswordChecker
      viewAs<MaterialProgressBar>().isVisible = true
      textView(ButtonCheck).clearText()
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
    
    private val TextError = View.generateViewId()
    private val ButtonCheck = View.generateViewId()
    
    val BaseFragmentScreen.checkMasterPasswordDialog get() = viewAs<PasswordStrengthDialog>()
    
    fun ViewGroup.CheckMasterPasswordDialog(
      onCheckSuccessful: () -> Unit,
      onDialogClosed: () -> Unit,
      block: CheckMasterPasswordDialog.() -> Unit = {}
    ) = withViewBuilder {
      child<CheckMasterPasswordDialog, ViewGroup.LayoutParams>(MatchParent, MatchParent, block) {
        classNameTag()
        this.onCheckSuccessful = onCheckSuccessful
        this.onDialogClosed = onDialogClosed
      }
    }
  }
}
