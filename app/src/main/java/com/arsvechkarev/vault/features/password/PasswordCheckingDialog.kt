package com.arsvechkarev.vault.features.password

import android.annotation.SuppressLint
import android.content.Context
import android.widget.EditText
import android.widget.TextView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.extensions.BaseTextWatcher
import com.arsvechkarev.vault.core.extensions.hideKeyboard
import com.arsvechkarev.vault.password.MasterPasswordChecker
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.ProgressBarSize
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.Styles
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Ints.dp
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.animateInvisible
import com.arsvechkarev.vault.viewdsl.animateVisible
import com.arsvechkarev.vault.viewdsl.backgroundRoundRect
import com.arsvechkarev.vault.viewdsl.childViewAs
import com.arsvechkarev.vault.viewdsl.font
import com.arsvechkarev.vault.viewdsl.invisible
import com.arsvechkarev.vault.viewdsl.marginHorizontal
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.padding
import com.arsvechkarev.vault.viewdsl.paddingHorizontal
import com.arsvechkarev.vault.viewdsl.paddingVertical
import com.arsvechkarev.vault.viewdsl.size
import com.arsvechkarev.vault.viewdsl.tag
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import com.arsvechkarev.vault.views.MaterialProgressBar
import com.arsvechkarev.vault.views.SimpleDialog

@SuppressLint("ViewConstructor")
class PasswordCheckingDialog(
  context: Context,
  private val threader: Threader,
  private val masterPasswordChecker: MasterPasswordChecker
) : SimpleDialog(context) {
  
  private val editText get() = childViewAs<EditText>(PasswordCheckingEditText)
  private val textError get() = childViewAs<TextView>(PasswordCheckingTextError)
  private val progressBar get() = childViewAs<MaterialProgressBar>(PasswordCheckingProgressBar)
  private var onSuccess: (masterPassword: String) -> Unit = {}
  
  init {
    withViewBuilder {
      tag(PasswordCheckingDialog)
      size(MatchParent, MatchParent)
      VerticalLayout(MatchParent, WrapContent) {
        marginHorizontal(30.dp)
        paddingHorizontal(16.dp)
        paddingVertical(24.dp)
        backgroundRoundRect(8.dp, Colors.Dialog)
        TextView(WrapContent, WrapContent, style = Styles.BoldTextView) {
          marginHorizontal(8.dp)
          textSize(TextSizes.H3)
          text(context.getString(R.string.text_enter_master_password))
        }
        child<EditText>(MatchParent, WrapContent) {
          tag(PasswordCheckingEditText)
          margins(top = 42.dp)
          font(Fonts.SegoeUi)
          textSize(TextSizes.H3)
          padding(8.dp)
          setHint(R.string.hint_password)
          setSingleLine()
        }
        FrameLayout(WrapContent, WrapContent) {
          TextView(WrapContent, WrapContent, style = Styles.BaseTextView) {
            tag(PasswordCheckingTextError)
            invisible()
            margins(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 32.dp)
            textColor(Colors.Error)
          }
          addView(MaterialProgressBar(context).apply {
            size(ProgressBarSize, ProgressBarSize)
            tag(PasswordCheckingProgressBar)
            invisible()
          })
        }
        TextView(MatchParent, WrapContent, style = Styles.ClickableButton()) {
          tag(PasswordCheckingContinueButton)
          text(R.string.text_continue)
          marginHorizontal(16.dp)
          onClick(::checkPassword)
        }
      }
    }
  }
  
  private val passwordTextWatcher = object : BaseTextWatcher {
    
    override fun onTextChange(text: String) = textError.text("")
  }
  
  fun initiatePasswordCheck(onSuccess: (masterPassword: String) -> Unit) {
    this.onSuccess = onSuccess
    show()
    editText.requestFocus()
  }
  
  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    editText.addTextChangedListener(passwordTextWatcher)
  }
  
  override fun onDetachedFromWindow() {
    editText.removeTextChangedListener(passwordTextWatcher)
    super.onDetachedFromWindow()
  }
  
  private fun checkPassword() {
    val password = editText.text.toString()
    if (password.isBlank()) return
    progressBar.animateVisible()
    textError.animateInvisible()
    threader.onBackground {
      val isCorrect = masterPasswordChecker.isCorrect(password)
      if (isCorrect) {
        threader.onMainThread {
          progressBar.animateInvisible()
          onSuccess(password)
          hide()
          context.hideKeyboard(editText)
        }
      } else {
        threader.onMainThread {
          progressBar.animateInvisible()
          textError.animateVisible()
          textError.text(R.string.text_password_is_incorrect)
        }
      }
    }
  }
  
  companion object {
    
    const val PasswordCheckingDialog = "PasswordCheckingDialog"
    const val PasswordCheckingTextError = "PasswordCheckingTextError"
    const val PasswordCheckingEditText = "PasswordCheckingEditText"
    const val PasswordCheckingProgressBar = "PasswordCheckingProgressBar"
    const val PasswordCheckingContinueButton = "PasswordCheckingContinueButton"
  }
}