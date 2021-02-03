package com.arsvechkarev.vault.features.initial_screen

import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.ViewSwitcher
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.AndroidThreader
import com.arsvechkarev.vault.core.MIN_PASSWORD_LENGTH
import com.arsvechkarev.vault.core.Singletons.masterPasswordChecker
import com.arsvechkarev.vault.core.Singletons.masterPasswordSaver
import com.arsvechkarev.vault.core.Singletons.passwordChecker
import com.arsvechkarev.vault.core.Singletons.userAuthSaver
import com.arsvechkarev.vault.core.extensions.BaseTextWatcher
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.cryptography.PasswordStatus
import com.arsvechkarev.vault.cryptography.PasswordStatus.EMPTY
import com.arsvechkarev.vault.cryptography.PasswordStatus.OK
import com.arsvechkarev.vault.cryptography.PasswordStatus.TOO_SHORT
import com.arsvechkarev.vault.cryptography.PasswordStatus.TOO_WEAK
import com.arsvechkarev.vault.cryptography.PasswordStrength
import com.arsvechkarev.vault.cryptography.PasswordStrength.MEDIUM
import com.arsvechkarev.vault.cryptography.PasswordStrength.STRONG
import com.arsvechkarev.vault.cryptography.PasswordStrength.VERY_STRONG
import com.arsvechkarev.vault.cryptography.PasswordStrength.WEAK
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Colors.CorrectRipple
import com.arsvechkarev.vault.viewbuilding.Colors.Dialog
import com.arsvechkarev.vault.viewbuilding.Colors.ErrorRipple
import com.arsvechkarev.vault.viewbuilding.Dimens.PasswordStrengthMeterHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.ProgressBarSizeBig
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableButton
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ImageBack
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Ints.dp
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.Size.IntSize
import com.arsvechkarev.vault.viewdsl.addView
import com.arsvechkarev.vault.viewdsl.animateInvisible
import com.arsvechkarev.vault.viewdsl.animateVisible
import com.arsvechkarev.vault.viewdsl.backgroundRoundRect
import com.arsvechkarev.vault.viewdsl.classNameTag
import com.arsvechkarev.vault.viewdsl.font
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.invisible
import com.arsvechkarev.vault.viewdsl.layoutGravity
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
import com.arsvechkarev.vault.viewdsl.visible
import com.arsvechkarev.vault.views.MaterialProgressBar
import com.arsvechkarev.vault.views.PasswordStrengthMeter
import com.arsvechkarev.vault.views.SimpleDialog

class StartScreen : Screen(), StartView {
  
  override fun buildLayout() = withViewBuilder {
    RootCoordinatorLayout(MatchParent, MatchParent) {
      HorizontalLayout(MatchParent, WrapContent) {
        tag(RepeatPasswordLayout)
        invisible()
        margins(start = 16.dp, end = 16.dp, top = 16.dp)
        ImageView(WrapContent, WrapContent, style = ImageBack) {
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
        child<PasswordStrengthMeter>(MatchParent, IntSize(PasswordStrengthMeterHeight)) {
          classNameTag()
          margins(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
        }
        child<ViewSwitcher>(MatchParent, WrapContent) {
          classNameTag()
          inAnimation = AnimationUtils.loadAnimation(contextNonNull, R.anim.slide_out_left)
          outAnimation = AnimationUtils.loadAnimation(contextNonNull, R.anim.slide_in_right)
          EditText(MatchParent, WrapContent) {
            tag(EditTextEnterPassword)
            marginHorizontal(12.dp)
            font(Fonts.SegoeUi)
            textSize(TextSizes.H3)
            padding(8.dp)
            setHint(R.string.hint_enter_password)
            setSingleLine()
          }
          EditText(MatchParent, WrapContent) {
            tag(EditTextRepeatPassword)
            marginHorizontal(12.dp)
            font(Fonts.SegoeUi)
            textSize(TextSizes.H3)
            padding(8.dp)
            setHint(R.string.hint_repeat_password)
            setSingleLine()
          }
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
      child<SimpleDialog>(MatchParent, MatchParent) {
        tag(DialogSavePasswordOrNot)
        VerticalLayout(WrapContent, WrapContent) {
          marginHorizontal(30.dp)
          paddingHorizontal(16.dp)
          paddingVertical(24.dp)
          backgroundRoundRect(8.dp, Dialog)
          TextView(WrapContent, WrapContent, style = BoldTextView) {
            gravity(Gravity.CENTER)
            textSize(TextSizes.H3)
            text(getString(R.string.text_do_you_want_to_save_password_locally))
          }
          TextView(WrapContent, WrapContent, style = BaseTextView) {
            margins(top = 16.dp)
            layoutGravity(Gravity.CENTER)
            text(getString(R.string.text_save_password_or_not))
          }
          TextView(WrapContent, WrapContent, ClickableTextView(CorrectRipple)) {
            margins(top = 16.dp)
            textColor(Colors.Correct)
            layoutGravity(Gravity.CENTER)
            gravity(Gravity.CENTER)
            text(getString(R.string.text_save_password_locally))
            onClick { presenter.savePasswordLocally() }
          }
          TextView(WrapContent, WrapContent, ClickableTextView(ErrorRipple)) {
            margins(top = 16.dp)
            textColor(Colors.Error)
            layoutGravity(Gravity.CENTER)
            gravity(Gravity.CENTER)
            text(getString(R.string.text_do_not_save_password))
            onClick { presenter.doNotSavePasswordLocally() }
          }
        }
      }
      child<SimpleDialog>(MatchParent, MatchParent) {
        tag(DialogProgressBar)
        addView {
          MaterialProgressBar(context).apply {
            size(ProgressBarSizeBig, ProgressBarSizeBig)
          }
        }
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
    StartPresenter(AndroidThreader, passwordChecker, masterPasswordSaver,
      masterPasswordChecker, userAuthSaver)
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
      EMPTY -> getString(R.string.text_password_cannot_be_empty)
      TOO_SHORT -> getString(R.string.text_password_min_length, MIN_PASSWORD_LENGTH)
      TOO_WEAK -> getString(R.string.text_password_is_too_weak)
      OK -> getString(R.string.text_empty)
    }
    textView(TextError).text(text)
  }
  
  override fun showPasswordStrength(strength: PasswordStrength?) {
    viewAs<PasswordStrengthMeter>().setStrength(strength)
    val textResId = when (strength) {
      WEAK -> R.string.text_weak
      MEDIUM -> R.string.text_medium
      STRONG -> R.string.text_strong
      VERY_STRONG -> R.string.text_secure
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
    simpleDialog(DialogSavePasswordOrNot).show()
    hideKeyboard()
  }
  
  override fun showPasswordsDontMatch() {
    textView(TextError).text(R.string.text_passwords_dont_match)
  }
  
  override fun showFinishingAuthorization() {
    simpleDialog(DialogProgressBar).show()
  }
  
  override fun goToPasswordsList() {
    navigator.goToPasswordsListScreen()
  }
  
  companion object {
    
    const val TextPasswordStrength = "TextPasswordStrength"
    const val TextError = "TextError"
    const val TextTitle = "TextTitle"
    const val TextContinue = "TextContinue"
    const val RepeatPasswordLayout = "RepeatPasswordLayout"
    const val EditTextEnterPassword = "EditTextEnterPassword"
    const val EditTextRepeatPassword = "EditTextRepeatPassword"
    const val DialogProgressBar = "DialogProgressBar"
    const val DialogSavePasswordOrNot = "DialogSavePasswordOrNot"
  }
}