package com.arsvechkarev.vault.features.initial_screen

import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.ViewSwitcher
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.AndroidThreader
import com.arsvechkarev.vault.core.MIN_PASSWORD_LENGTH
import com.arsvechkarev.vault.core.Singletons.masterPasswordChecker
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
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginMedium
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.PasswordStrengthMeterHeight
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableButton
import com.arsvechkarev.vault.viewbuilding.Styles.ImageBack
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.Size.IntSize
import com.arsvechkarev.vault.viewdsl.animateInvisible
import com.arsvechkarev.vault.viewdsl.animateVisible
import com.arsvechkarev.vault.viewdsl.classNameTag
import com.arsvechkarev.vault.viewdsl.font
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.id
import com.arsvechkarev.vault.viewdsl.invisible
import com.arsvechkarev.vault.viewdsl.layoutGravity
import com.arsvechkarev.vault.viewdsl.marginHorizontal
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.padding
import com.arsvechkarev.vault.viewdsl.tag
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.viewdsl.visible
import com.arsvechkarev.vault.views.PasswordStrengthMeter
import com.arsvechkarev.vault.views.dialogs.LoadingDialog
import com.arsvechkarev.vault.views.dialogs.loadingDialog

class StartScreen : Screen(), StartView {
  
  override fun buildLayout() = withViewBuilder {
    RootCoordinatorLayout(MatchParent, MatchParent) {
      HorizontalLayout(MatchParent, WrapContent) {
        tag(RepeatPasswordLayout)
        invisible()
        margins(top = MarginDefault, start = MarginDefault, end = MarginDefault)
        ImageView(WrapContent, WrapContent, style = ImageBack) {
          onClick { presenter.onBackButtonClick() }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          layoutGravity(Gravity.CENTER)
          text(R.string.text_repeat_password)
          margins(start = MarginMedium, end = MarginMedium)
          gravity(Gravity.CENTER)
          textSize(TextSizes.H1)
        }
      }
      TextView(MatchParent, WrapContent, style = BoldTextView) {
        id(R.id.start_screen_create_password)
        tag(TextTitle)
        text(R.string.text_create_master_password)
        margins(start = MarginDefault, end = MarginDefault, top = MarginMedium)
        gravity(Gravity.CENTER)
        textSize(TextSizes.H1)
      }
      VerticalLayout(MatchParent, WrapContent) {
        layoutGravity(Gravity.CENTER)
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          tag(TextPasswordStrength)
          margins(start = MarginDefault)
        }
        child<PasswordStrengthMeter>(MatchParent, IntSize(PasswordStrengthMeterHeight)) {
          classNameTag()
          margins(top = MarginDefault, start = MarginDefault,
            end = MarginDefault, bottom = MarginMedium)
        }
        child<ViewSwitcher>(MatchParent, WrapContent) {
          classNameTag()
          inAnimation = AnimationUtils.loadAnimation(contextNonNull, R.anim.slide_out_left)
          outAnimation = AnimationUtils.loadAnimation(contextNonNull, R.anim.slide_in_right)
          EditText(MatchParent, WrapContent) {
            tag(EditTextEnterPassword)
            marginHorizontal(MarginDefault)
            font(Fonts.SegoeUi)
            textSize(TextSizes.H3)
            padding(MarginSmall)
            setHint(R.string.hint_enter_password)
            setSingleLine()
          }
          EditText(MatchParent, WrapContent) {
            tag(EditTextRepeatPassword)
            marginHorizontal(MarginDefault)
            font(Fonts.SegoeUi)
            textSize(TextSizes.H3)
            padding(MarginSmall)
            setHint(R.string.hint_repeat_password)
            setSingleLine()
          }
        }
        TextView(MatchParent, WrapContent, style = BaseTextView) {
          tag(TextError)
          textColor(Colors.Error)
          margins(start = MarginDefault, end = MarginDefault, top = MarginDefault)
        }
      }
      TextView(MatchParent, WrapContent, style = ClickableButton()) {
        id(R.id.start_screen_continue)
        tag(TextContinue)
        layoutGravity(Gravity.BOTTOM)
        text(R.string.text_continue)
        margins(start = MarginDefault, end = MarginDefault, bottom = MarginDefault)
        whenPresenterIsReady {
          onClick {
            presenter.onEnteredPassword(editText(EditTextEnterPassword).text.toString())
          }
        }
      }
      LoadingDialog()
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
    StartPresenter(AndroidThreader, passwordChecker, masterPasswordChecker,
      userAuthSaver)
  }
  
  override fun onInit() {
    editText(EditTextEnterPassword).addTextChangedListener(passwordTextWatcher)
    editText(EditTextEnterPassword).addTextChangedListener(clearErrorTextWatcher)
    editText(EditTextRepeatPassword).addTextChangedListener(clearErrorTextWatcher)
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
  
  override fun showPasswordsDontMatch() {
    textView(TextError).text(R.string.text_passwords_dont_match)
  }
  
  override fun showFinishingAuthorization() {
    hideKeyboard()
    loadingDialog().show()
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
  }
}