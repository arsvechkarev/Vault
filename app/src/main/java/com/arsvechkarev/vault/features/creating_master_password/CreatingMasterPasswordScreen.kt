package com.arsvechkarev.vault.features.creating_master_password

import android.content.Context
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.animation.AnimationUtils
import android.widget.ViewSwitcher
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.MIN_PASSWORD_LENGTH
import com.arsvechkarev.vault.core.di.CoreComponent
import com.arsvechkarev.vault.core.extensions.moxyPresenter
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
import com.arsvechkarev.vault.viewbuilding.Dimens.PasswordStrengthMeterHeight
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableButton
import com.arsvechkarev.vault.viewbuilding.Styles.ImageBack
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.BaseTextWatcher
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.Size.IntSize
import com.arsvechkarev.vault.viewdsl.animateInvisible
import com.arsvechkarev.vault.viewdsl.animateVisible
import com.arsvechkarev.vault.viewdsl.backgroundColor
import com.arsvechkarev.vault.viewdsl.classNameTag
import com.arsvechkarev.vault.viewdsl.clearOnClick
import com.arsvechkarev.vault.viewdsl.drawablePadding
import com.arsvechkarev.vault.viewdsl.drawables
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.hideKeyboard
import com.arsvechkarev.vault.viewdsl.invisible
import com.arsvechkarev.vault.viewdsl.layoutGravity
import com.arsvechkarev.vault.viewdsl.marginHorizontal
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.showKeyboard
import com.arsvechkarev.vault.viewdsl.tag
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.viewdsl.visible
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import com.arsvechkarev.vault.views.EditTextPassword
import com.arsvechkarev.vault.views.PasswordStrengthMeter
import com.arsvechkarev.vault.views.dialogs.LoadingDialog
import com.arsvechkarev.vault.views.dialogs.PasswordStrengthDialog.Companion.PasswordStrengthDialog
import com.arsvechkarev.vault.views.dialogs.PasswordStrengthDialog.Companion.passwordStrengthDialog
import com.arsvechkarev.vault.views.dialogs.loadingDialog
import navigation.BaseScreen

class CreatingMasterPasswordScreen : BaseScreen(), CreatingMasterPasswordView {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootCoordinatorLayout(MatchParent, MatchParent) {
      backgroundColor(Colors.Background)
      HorizontalLayout(MatchParent, WrapContent) {
        tag(RepeatPasswordLayout)
        invisible()
        margins(top = MarginDefault + StatusBarHeight, start = MarginDefault, end = MarginDefault)
        ImageView(WrapContent, WrapContent, style = ImageBack) {
          onClick { presenter.onBackButtonClick() }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          layoutGravity(CENTER)
          text(R.string.text_repeat_password)
          margins(start = MarginMedium, end = MarginMedium)
          gravity(CENTER)
          textSize(TextSizes.H1)
        }
      }
      TextView(MatchParent, WrapContent, style = BoldTextView) {
        tag(TextTitle)
        text(R.string.text_create_master_password)
        margins(top = MarginDefault + StatusBarHeight, start = MarginDefault, end = MarginDefault)
        gravity(CENTER)
        textSize(TextSizes.H1)
      }
      VerticalLayout(MatchParent, WrapContent) {
        layoutGravity(CENTER)
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
          inAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
          outAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
          child<EditTextPassword>(MatchParent, WrapContent) {
            tag(EditTextEnterPassword)
            marginHorizontal(MarginDefault)
            setHint(R.string.hint_enter_password)
          }
          child<EditTextPassword>(MatchParent, WrapContent) {
            tag(EditTextRepeatPassword)
            marginHorizontal(MarginDefault)
            setHint(R.string.hint_repeat_password)
          }
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          tag(TextError)
          gravity(CENTER)
          drawablePadding(MarginDefault)
          drawables(end = R.drawable.ic_question, color = Colors.Background)
          textColor(Colors.Error)
          margins(start = MarginDefault, end = MarginDefault, top = MarginDefault)
        }
      }
      TextView(MatchParent, WrapContent, style = ClickableButton()) {
        tag(TextContinue)
        layoutGravity(Gravity.BOTTOM)
        text(R.string.text_continue)
        margins(start = MarginDefault, end = MarginDefault, bottom = MarginDefault)
        onClick {
          presenter.onEnteredPassword(viewAs<EditTextPassword>(EditTextEnterPassword).getText())
        }
      }
      LoadingDialog()
      PasswordStrengthDialog {
        onHide = { presenter.onHidePasswordStrengthDialog() }
        onGotItClicked { presenter.onHidePasswordStrengthDialog() }
      }
    }
  }
  
  private val clearErrorTextWatcher = object : BaseTextWatcher {
    
    override fun onTextChange(text: String) {
      textView(TextError).clearOnClick()
      textView(TextError).invisible()
    }
  }
  
  private val passwordTextWatcher = object : BaseTextWatcher {
    
    override fun onTextChange(text: String) {
      presenter.computePasswordStrength(text)
    }
  }
  
  private val presenter by moxyPresenter {
    CoreComponent.instance.getCreatingMasterPasswordComponentFactory().create().providePresenter()
  }
  
  override fun onInit() {
    viewAs<EditTextPassword>(EditTextEnterPassword).addTextChangedListener(passwordTextWatcher)
    viewAs<EditTextPassword>(EditTextEnterPassword).addTextChangedListener(clearErrorTextWatcher)
    viewAs<EditTextPassword>(EditTextRepeatPassword).addTextChangedListener(clearErrorTextWatcher)
  }
  
  override fun onAppearedOnScreenAfterAnimation() {
    contextNonNull.showKeyboard()
    viewAs<EditTextPassword>(EditTextEnterPassword).requestEditTextFocus()
  }
  
  override fun handleBackPress(): Boolean {
    return presenter.handleBackPress()
  }
  
  override fun onRelease() {
    super.onRelease()
    viewAs<EditTextPassword>(EditTextRepeatPassword).removeTextChangedListener(passwordTextWatcher)
    viewAs<EditTextPassword>(EditTextEnterPassword).removeTextChangedListener(clearErrorTextWatcher)
    viewAs<EditTextPassword>(EditTextRepeatPassword).removeTextChangedListener(
      clearErrorTextWatcher)
  }
  
  override fun showPasswordProblem(passwordStatus: PasswordStatus) {
    val text = when (passwordStatus) {
      EMPTY -> contextNonNull.getString(R.string.text_password_cannot_be_empty)
      TOO_SHORT -> contextNonNull.getString(R.string.text_password_min_length, MIN_PASSWORD_LENGTH)
      TOO_WEAK -> contextNonNull.getString(R.string.text_password_is_too_weak)
      OK -> contextNonNull.getString(R.string.text_empty)
    }
    textView(TextError).visible()
    if (passwordStatus == TOO_WEAK) {
      textView(TextError).drawables(end = R.drawable.ic_question, color = Colors.Error)
      textView(TextError).onClick { presenter.onShowPasswordStrengthDialog() }
    } else {
      textView(TextError).drawables(end = R.drawable.ic_question, color = Colors.Background)
      textView(TextError).onClick {}
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
  
  override fun showPasswordStrengthDialog() {
    passwordStrengthDialog.show()
  }
  
  override fun hidePasswordStrengthDialog() {
    passwordStrengthDialog.hide()
  }
  
  override fun switchToEnterPasswordState() {
    textView(TextContinue).onClick {
      presenter.onEnteredPassword(viewAs<EditTextPassword>(EditTextEnterPassword).getText())
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
      presenter.onRepeatedPassword(viewAs<EditTextPassword>(EditTextRepeatPassword).getText())
    }
    viewAs<EditTextPassword>(EditTextRepeatPassword).text("")
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
    contextNonNull.hideKeyboard()
    loadingDialog.show()
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