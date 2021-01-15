package com.arsvechkarev.vault.features.start

import android.view.Gravity
import android.widget.EditText
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.AndroidThreader
import com.arsvechkarev.vault.core.extensions.BaseTextWatcher
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.core.password.PasswordStrength
import com.arsvechkarev.vault.core.password.PasswordStrength.MEDIUM
import com.arsvechkarev.vault.core.password.PasswordStrength.STRONG
import com.arsvechkarev.vault.core.password.PasswordStrength.VERY_STRONG
import com.arsvechkarev.vault.core.password.PasswordStrength.WEAK
import com.arsvechkarev.vault.core.password.ZxcvbnPasswordChecker
import com.arsvechkarev.vault.core.viewbuilding.Colors
import com.arsvechkarev.vault.core.viewbuilding.Fonts
import com.arsvechkarev.vault.core.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.core.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.core.viewbuilding.Styles.ClickableButton
import com.arsvechkarev.vault.core.viewbuilding.TextSizes
import com.arsvechkarev.vault.core.viewdsl.Ints.dp
import com.arsvechkarev.vault.core.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.core.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.core.viewdsl.Size.IntSize
import com.arsvechkarev.vault.core.viewdsl.classNameTag
import com.arsvechkarev.vault.core.viewdsl.font
import com.arsvechkarev.vault.core.viewdsl.gravity
import com.arsvechkarev.vault.core.viewdsl.layoutGravity
import com.arsvechkarev.vault.core.viewdsl.margin
import com.arsvechkarev.vault.core.viewdsl.marginHorizontal
import com.arsvechkarev.vault.core.viewdsl.margins
import com.arsvechkarev.vault.core.viewdsl.padding
import com.arsvechkarev.vault.core.viewdsl.tag
import com.arsvechkarev.vault.core.viewdsl.text
import com.arsvechkarev.vault.core.viewdsl.textColor
import com.arsvechkarev.vault.core.viewdsl.textSize
import com.arsvechkarev.vault.views.PasswordStrengthMeter
import com.nulabinc.zxcvbn.Zxcvbn

class StartScreen : Screen(), StartView {
  
  override fun buildLayout() = withViewBuilder {
    RootCoordinatorLayout(MatchParent, MatchParent) {
      TextView(MatchParent, WrapContent, style = BoldTextView) {
        text(R.string.text_create_master_password)
        margins(top = 24.dp)
        gravity(Gravity.CENTER)
        textSize(TextSizes.H0)
      }
      VerticalLayout(MatchParent, WrapContent) {
        layoutGravity(Gravity.CENTER)
        TextView(MatchParent, WrapContent) {
          
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          tag(TextPasswordStrength)
          margins(start = 16.dp)
        }
        child<PasswordStrengthMeter>(MatchParent, IntSize(8.dp)) {
          classNameTag()
          margins(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
        }
        child<EditText>(MatchParent, WrapContent) {
          tag(EditTextPassword)
          marginHorizontal(12.dp)
          font(Fonts.SegoeUi)
          textSize(TextSizes.H3)
          padding(8.dp)
          setHint(R.string.hint_enter_password)
          setSingleLine()
        }
        child<EditText>(MatchParent, WrapContent) {
          tag(EditTextRepeatPassword)
          margins(start = 12.dp, end = 12.dp, top = 24.dp)
          font(Fonts.SegoeUi)
          textSize(TextSizes.H3)
          padding(8.dp)
          setHint(R.string.hint_repeat_password)
          setSingleLine()
        }
        TextView(MatchParent, WrapContent, style = BaseTextView) {
          tag(TextError)
          textColor(Colors.Error)
          margin(16.dp)
          //          text("Password is really weak")
        }
      }
      TextView(MatchParent, WrapContent, style = ClickableButton()) {
        layoutGravity(Gravity.BOTTOM)
        text(R.string.text_continue)
        margins(start = 16.dp, end = 16.dp, bottom = 16.dp)
      }
    }
  }
  
  private val passwordTextWatcher = object : BaseTextWatcher() {
    
    override fun onTextChange(text: String) {
      presenter.computePasswordStrength(text)
    }
  }
  
  private val presenter by moxyPresenter {
    StartPresenter(AndroidThreader, ZxcvbnPasswordChecker(Zxcvbn()))
  }
  
  override fun onInit() {
    editText(EditTextPassword).addTextChangedListener(passwordTextWatcher)
  }
  
  override fun onRelease() {
    editText(EditTextRepeatPassword).removeTextChangedListener(passwordTextWatcher)
  }
  
  override fun showLoading() {
  
  }
  
  override fun showPasswordIsWrong() {
  
  }
  
  override fun showPasswordStrength(strength: PasswordStrength?) {
    viewAs<PasswordStrengthMeter>().setStrength(strength)
    val textResId = when (strength) {
      WEAK -> R.string.text_weak
      MEDIUM -> R.string.text_medium
      STRONG -> R.string.text_strong
      VERY_STRONG -> R.string.text_very_strong
      null -> R.string.text_empty
    }
    textView(TextPasswordStrength).text(textResId)
  }
  
  companion object {
    
    const val TextPasswordStrength = "TextPasswordStrength"
    const val TextError = "TextError"
    const val EditTextPassword = "EditTextPassword"
    const val EditTextRepeatPassword = "EditTextRepeatPassword"
  }
}