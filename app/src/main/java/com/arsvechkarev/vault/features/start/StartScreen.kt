package com.arsvechkarev.vault.features.start

import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ViewSwitcher
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.AndroidThreader
import com.arsvechkarev.vault.core.MIN_PASSWORD_LENGTH
import com.arsvechkarev.vault.core.extensions.BaseTextWatcher
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.core.password.PasswordStatus
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
import com.arsvechkarev.vault.core.viewdsl.animateInvisible
import com.arsvechkarev.vault.core.viewdsl.animateVisible
import com.arsvechkarev.vault.core.viewdsl.circleRippleBackground
import com.arsvechkarev.vault.core.viewdsl.classNameTag
import com.arsvechkarev.vault.core.viewdsl.font
import com.arsvechkarev.vault.core.viewdsl.gravity
import com.arsvechkarev.vault.core.viewdsl.image
import com.arsvechkarev.vault.core.viewdsl.invisible
import com.arsvechkarev.vault.core.viewdsl.layoutGravity
import com.arsvechkarev.vault.core.viewdsl.marginHorizontal
import com.arsvechkarev.vault.core.viewdsl.margins
import com.arsvechkarev.vault.core.viewdsl.onClick
import com.arsvechkarev.vault.core.viewdsl.padding
import com.arsvechkarev.vault.core.viewdsl.size
import com.arsvechkarev.vault.core.viewdsl.tag
import com.arsvechkarev.vault.core.viewdsl.text
import com.arsvechkarev.vault.core.viewdsl.textColor
import com.arsvechkarev.vault.core.viewdsl.textSize
import com.arsvechkarev.vault.core.viewdsl.visible
import com.arsvechkarev.vault.views.PasswordStrengthMeter
import com.nulabinc.zxcvbn.Zxcvbn

class StartScreen : Screen(), StartView {
  
  override fun buildLayout() = withViewBuilder {
    RootCoordinatorLayout(MatchParent, MatchParent) {
      HorizontalLayout(MatchParent, WrapContent) {
        tag(RepeatPasswordLayout)
        invisible()
        margins(start = 16.dp, end = 16.dp, top = 24.dp)
        ImageView(WrapContent, WrapContent) {
          tag(ImageBack)
          padding(8.dp)
          circleRippleBackground(Colors.Ripple)
          image(R.drawable.ic_back)
          onClick { presenter.onBackButtonClick() }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          layoutGravity(Gravity.CENTER)
          text(R.string.text_repeat_password)
          margins(start = 24.dp, end = 24.dp)
          gravity(Gravity.CENTER)
          textSize(TextSizes.H0)
        }
      }
      TextView(MatchParent, WrapContent, style = BoldTextView) {
        tag(TextTitle)
        text(R.string.text_create_master_password)
        margins(start = 24.dp, end = 24.dp, top = 24.dp)
        gravity(Gravity.CENTER)
        textSize(TextSizes.H0)
      }
      VerticalLayout(MatchParent, WrapContent) {
        layoutGravity(Gravity.CENTER)
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          tag(TextPasswordStrength)
          margins(start = 16.dp)
        }
        child<PasswordStrengthMeter>(MatchParent, IntSize(6.dp)) {
          classNameTag()
          margins(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
        }
        child<ViewSwitcher>(MatchParent, WrapContent) {
          classNameTag()
          inAnimation = AnimationUtils.loadAnimation(contextNonNull, R.anim.slide_out_left)
          outAnimation = AnimationUtils.loadAnimation(contextNonNull, R.anim.slide_in_right)
          addView(EditText(context).apply {
            size(MatchParent, WrapContent)
            tag(EditTextEnterPassword)
            marginHorizontal(12.dp)
            font(Fonts.SegoeUi)
            textSize(TextSizes.H3)
            padding(8.dp)
            setHint(R.string.hint_enter_password)
            setSingleLine()
          })
          addView(EditText(context).apply {
            size(MatchParent, WrapContent)
            tag(EditTextRepeatPassword)
            marginHorizontal(12.dp)
            font(Fonts.SegoeUi)
            textSize(TextSizes.H3)
            padding(8.dp)
            setHint(R.string.hint_repeat_password)
            setSingleLine()
          })
        }
        TextView(MatchParent, WrapContent, style = BaseTextView) {
          tag(TextError)
          textColor(Colors.Error)
          margins(start = 16.dp, end = 16.dp, top = 16.dp)
        }
      }
      TextView(MatchParent, WrapContent, style = ClickableButton()) {
        tag(TextContinue)
        layoutGravity(Gravity.BOTTOM)
        text(R.string.text_continue)
        margins(start = 16.dp, end = 16.dp, bottom = 16.dp)
      }
    }
  }
  
  private val clearErrorTextWatcher = object : BaseTextWatcher {
    
    override fun onTextChange(text: String) {
      textView(TextError).text("")
    }
  }
  
  private val passwordTextWatcher = object : BaseTextWatcher {
    
    override fun onTextChange(text: String) {
      presenter.computePasswordStrength(text)
    }
  }
  
  private val presenter by moxyPresenter {
    StartPresenter(AndroidThreader, ZxcvbnPasswordChecker(Zxcvbn()))
  }
  
  override fun onInit() {
    editText(EditTextEnterPassword).addTextChangedListener(passwordTextWatcher)
    editText(EditTextEnterPassword).addTextChangedListener(clearErrorTextWatcher)
    editText(EditTextRepeatPassword).addTextChangedListener(clearErrorTextWatcher)
    textView(TextContinue).onClick {
      presenter.onEnteredPassword(editText(EditTextEnterPassword).text.toString())
    }
  }
  
  override fun allowBackPress(): Boolean {
    return presenter.allowBackPress()
  }
  
  override fun onRelease() {
    editText(EditTextRepeatPassword).removeTextChangedListener(passwordTextWatcher)
    editText(EditTextEnterPassword).removeTextChangedListener(clearErrorTextWatcher)
    editText(EditTextRepeatPassword).removeTextChangedListener(clearErrorTextWatcher)
  }
  
  override fun showPasswordProblem(passwordStatus: PasswordStatus) {
    val text = when (passwordStatus) {
      PasswordStatus.EMPTY -> getString(R.string.text_password_cannot_be_empty)
      PasswordStatus.TOO_SHORT -> getString(R.string.text_password_min_length, MIN_PASSWORD_LENGTH)
      PasswordStatus.TOO_WEAK -> getString(R.string.text_password_is_too_weak)
      PasswordStatus.OK -> getString(R.string.text_empty)
    }
    textView(TextError).text(text)
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
  
  override fun switchToEnterPasswordState() {
    textView(TextContinue).onClick {
      presenter.onEnteredPassword(editText(EditTextEnterPassword).text.toString())
    }
    viewAs<PasswordStrengthMeter>().setStrength(null, animate = false)
    textView(TextError).text("")
    viewAs<PasswordStrengthMeter>().visible()
    view(TextTitle).animateVisible()
    view(RepeatPasswordLayout).animateInvisible()
    textView(TextTitle).text(R.string.text_create_master_password)
    viewAs<ViewSwitcher>().apply {
      inAnimation = AnimationUtils.loadAnimation(contextNonNull, android.R.anim.slide_in_left)
      outAnimation = AnimationUtils.loadAnimation(contextNonNull, android.R.anim.slide_out_right)
      showPrevious()
    }
  }
  
  override fun switchToRepeatPasswordState() {
    textView(TextContinue).onClick {
      presenter.onRepeatedPassword(editText(EditTextRepeatPassword).text.toString())
    }
    editText(EditTextRepeatPassword).text("")
    textView(TextError).text("")
    textView(TextPasswordStrength).text("")
    view(TextTitle).animateInvisible()
    view(RepeatPasswordLayout).animateVisible()
    viewAs<PasswordStrengthMeter>().invisible()
    viewAs<ViewSwitcher>().apply {
      inAnimation = AnimationUtils.loadAnimation(contextNonNull, R.anim.slide_out_left)
      outAnimation = AnimationUtils.loadAnimation(contextNonNull, R.anim.slide_in_right)
      showNext()
    }
  }
  
  override fun showPasswordRepeatedCorrectly() {
    textView(TextError).text("Passwords match")
  }
  
  override fun showPasswordsDontMatch() {
    textView(TextError).text(R.string.text_passwords_dont_match)
  }
  
  companion object {
    
    const val TextPasswordStrength = "TextPasswordStrength"
    const val TextError = "TextError"
    const val TextTitle = "TextTitle"
    const val TextContinue = "TextContinue"
    const val ImageBack = "ImageBack"
    const val RepeatPasswordLayout = "RepeatPasswordLayout"
    const val EditTextEnterPassword = "EditTextEnterPassword"
    const val EditTextRepeatPassword = "EditTextRepeatPassword"
  }
}