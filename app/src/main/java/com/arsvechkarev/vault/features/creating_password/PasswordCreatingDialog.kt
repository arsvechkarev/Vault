package com.arsvechkarev.vault.features.creating_password

import android.content.Context
import android.view.Gravity
import android.view.Gravity.BOTTOM
import android.view.Gravity.CENTER
import android.view.Gravity.END
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.TextView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.R.string.text_numbers
import com.arsvechkarev.vault.R.string.text_special_symbols
import com.arsvechkarev.vault.R.string.text_uppercase_symbols
import com.arsvechkarev.vault.core.DEFAULT_PASSWORD_LENGTH
import com.arsvechkarev.vault.core.MAX_PASSWORD_LENGTH
import com.arsvechkarev.vault.core.MIN_PASSWORD_LENGTH
import com.arsvechkarev.vault.core.extensions.BaseTextWatcher
import com.arsvechkarev.vault.core.extensions.hideKeyboard
import com.arsvechkarev.vault.core.extensions.setSoftInputMode
import com.arsvechkarev.vault.core.model.PasswordCharacteristics
import com.arsvechkarev.vault.core.model.PasswordCharacteristics.NUMBERS
import com.arsvechkarev.vault.core.model.PasswordCharacteristics.SPECIAL_SYMBOLS
import com.arsvechkarev.vault.core.model.PasswordCharacteristics.UPPERCASE_SYMBOLS
import com.arsvechkarev.vault.views.dialogs.InfoDialog
import com.arsvechkarev.vault.views.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.cryptography.PasswordStrength
import com.arsvechkarev.vault.cryptography.PasswordStrength.MEDIUM
import com.arsvechkarev.vault.cryptography.PasswordStrength.STRONG
import com.arsvechkarev.vault.cryptography.PasswordStrength.VERY_STRONG
import com.arsvechkarev.vault.cryptography.PasswordStrength.WEAK
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.IconPadding
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageBackMargin
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginBig
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.childViewAs
import com.arsvechkarev.vault.viewdsl.circleRippleBackground
import com.arsvechkarev.vault.viewdsl.classNameTag
import com.arsvechkarev.vault.viewdsl.drawablePadding
import com.arsvechkarev.vault.viewdsl.drawables
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.image
import com.arsvechkarev.vault.viewdsl.layoutGravity
import com.arsvechkarev.vault.viewdsl.margin
import com.arsvechkarev.vault.viewdsl.marginHorizontal
import com.arsvechkarev.vault.viewdsl.marginVertical
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.onProgressChanged
import com.arsvechkarev.vault.viewdsl.padding
import com.arsvechkarev.vault.viewdsl.setMaxLength
import com.arsvechkarev.vault.viewdsl.tag
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import com.arsvechkarev.vault.views.CheckmarkAndTextViewGroup
import com.arsvechkarev.vault.views.CheckmarkAndTextViewGroup.Companion.CheckmarkAndTextViewGroup
import com.arsvechkarev.vault.views.PasswordStrengthMeterWithText
import com.arsvechkarev.vault.views.SimpleDialog

class PasswordCreatingDialog(
  context: Context,
  private val presenter: PasswordCreatingPresenter
) : FrameLayout(context), PasswordCreatingView {
  
  init {
    withViewBuilder {
      child<SimpleDialog>(MatchParent, MatchParent) {
        tag(DialogPassword)
        setShadowColor(Colors.Background)
        onShown = { context.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING) }
        onHide = {
          context.setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)
          context.hideKeyboard(childViewAs(DialogPasswordEditText))
        }
        FrameLayout(MatchParent, MatchParent) {
          VerticalLayout(MatchParent, MatchParent) {
            FrameLayout(MatchParent, WrapContent) {
              margins(top = MarginSmall)
              TextView(WrapContent, WrapContent, style = BoldTextView) {
                tag(DialogPasswordTitle)
                text(R.string.text_edit_password)
                textSize(TextSizes.H1)
                layoutGravity(CENTER)
              }
              ImageView(WrapContent, WrapContent) {
                image(R.drawable.ic_cross)
                circleRippleBackground()
                margins(end = ImageBackMargin, top = MarginSmall, bottom = MarginSmall)
                layoutGravity(CENTER or END)
                padding(IconPadding)
              }
            }
            TextView(WrapContent, WrapContent, style = BaseTextView) {
              tag(DialogPasswordErrorText)
              layoutGravity(Gravity.CENTER_HORIZONTAL)
              textColor(Colors.Error)
            }
            EditText(MatchParent, WrapContent, style = Styles.BaseEditText) {
              tag(DialogPasswordEditText)
              gravity(CENTER)
              margin(MarginSmall)
              setMaxLength(MAX_PASSWORD_LENGTH)
              isSingleLine = false
            }
            child<PasswordStrengthMeterWithText>(MatchParent, WrapContent) {
              classNameTag()
              margin(MarginDefault)
            }
            val commonBlock: CheckmarkAndTextViewGroup.() -> Unit = {
              onClick {
                val characteristics = when (tag as Int) {
                  text_uppercase_symbols -> UPPERCASE_SYMBOLS
                  text_numbers -> NUMBERS
                  text_special_symbols -> SPECIAL_SYMBOLS
                  else -> throw IllegalStateException("Unknown tag")
                }
                presenter.onCheckmarkClicked(characteristics)
              }
            }
            CheckmarkAndTextViewGroup(text_uppercase_symbols, commonBlock)
            CheckmarkAndTextViewGroup(text_numbers, commonBlock)
            CheckmarkAndTextViewGroup(text_special_symbols, commonBlock)
            TextView(WrapContent, WrapContent, style = BoldTextView) {
              tag(DialogPasswordTextLength)
              margins(start = MarginDefault, top = MarginBig)
              textSize(TextSizes.H3)
              text(context.getString(R.string.text_password_length, DEFAULT_PASSWORD_LENGTH))
            }
            child<SeekBar>(MatchParent, WrapContent) {
              marginHorizontal(MarginBig)
              marginVertical(MarginDefault)
              max = MAX_PASSWORD_LENGTH - MIN_PASSWORD_LENGTH
              progress = DEFAULT_PASSWORD_LENGTH - MIN_PASSWORD_LENGTH
              onProgressChanged(presenter::onPasswordLengthChanged)
            }
            TextView(WrapContent, WrapContent, style = ClickableTextView()) {
              margins(top = MarginDefault)
              layoutGravity(Gravity.CENTER_HORIZONTAL)
              drawables(start = R.drawable.ic_generate, color = Colors.AccentLight)
              drawablePadding(MarginSmall)
              textColor(Colors.AccentLight)
              text(context.getString(R.string.text_generate_password))
              onClick { presenter.onGeneratePasswordClicked() }
            }
          }
          TextView(WrapContent, WrapContent, style = Styles.ClickableButton()) {
            layoutGravity(CENTER or BOTTOM)
            margin(MarginDefault)
            textSize(TextSizes.H3)
            text(R.string.text_save_password)
            onClick { presenter.onSavePasswordClicked() }
          }
        }
      }
      InfoDialog(tagPrefix = PasswordCreatingDialog::class.java.name) {
        classNameTag()
      }
    }
  }
  
  private var onPasswordCreated: (String) -> Unit = {}
  
  private val passwordTextWatcher = object : BaseTextWatcher {
    
    override fun onTextChange(text: String) {
      childViewAs<TextView>(DialogPasswordErrorText).text("")
      presenter.onPasswordChanged(text)
    }
  }
  
  fun initiatePasswordCreation(onPasswordCreated: (String) -> Unit) {
    this.onPasswordCreated = onPasswordCreated
    childViewAs<EditText>(DialogPasswordEditText).addTextChangedListener(passwordTextWatcher)
    childViewAs<SimpleDialog>(DialogPassword).show()
    presenter.showInitialGeneratedPassword()
  }
  
  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    presenter.attachView(this)
  }
  
  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    presenter.detachView()
    childViewAs<EditText>(DialogPasswordEditText).removeTextChangedListener(passwordTextWatcher)
  }
  
  override fun showChangePasswordLength(progress: Int) {
    val text = context.getString(R.string.text_password_length, progress)
    childViewAs<TextView>(DialogPasswordTextLength).text(text)
  }
  
  override fun showPasswordStrength(strength: PasswordStrength?) {
    val textResId = when (strength) {
      null, WEAK -> R.string.text_weak
      MEDIUM -> R.string.text_medium
      STRONG -> R.string.text_strong
      VERY_STRONG -> R.string.text_secure
    }
    childViewAs<PasswordStrengthMeterWithText>().setText(textResId)
    childViewAs<PasswordStrengthMeterWithText>().setStrength(strength ?: WEAK)
  }
  
  override fun showPasswordCharacteristics(characteristics: Collection<PasswordCharacteristics>) {
    val checkmark: (Int) -> CheckmarkAndTextViewGroup = { textResId -> childViewAs(textResId) }
    checkmark(text_uppercase_symbols).isChecked = characteristics.contains(UPPERCASE_SYMBOLS)
    checkmark(text_numbers).isChecked = characteristics.contains(NUMBERS)
    checkmark(text_special_symbols).isChecked = characteristics.contains(SPECIAL_SYMBOLS)
  }
  
  override fun showGeneratedPassword(password: String) {
    context.hideKeyboard(childViewAs(DialogPasswordEditText))
    childViewAs<EditText>(DialogPasswordEditText).clearFocus()
    childViewAs<EditText>(DialogPasswordEditText).text(password)
  }
  
  override fun showPasswordIsEmpty() {
    childViewAs<TextView>(DialogPasswordErrorText).text(R.string.text_password_cannot_be_empty)
  }
  
  override fun showPasswordIsTooWeak() {
    childViewAs<TextView>(DialogPasswordErrorText).text(R.string.text_password_is_too_weak)
  }
  
  override fun showPasswordIsTooShort() {
    val text = context.getString(R.string.text_password_min_length, MIN_PASSWORD_LENGTH)
    childViewAs<TextView>(DialogPasswordErrorText).text(text)
  }
  
  override fun showDialogAcceptNewPassword() {
    childViewAs<InfoDialog>().show(
      R.string.text_saving_password,
      R.string.text_do_you_want_to_save_password,
      R.string.text_yes,
      onOkClicked = { presenter.acceptNewPassword() }
    )
  }
  
  override fun showNewPasswordIsSaved(password: String) {
    onPasswordCreated(password)
  }
  
  companion object {
    
    const val DialogPassword = "DialogPassword"
    const val DialogPasswordTextLength = "DialogPasswordTextLength"
    const val DialogPasswordTitle = "DialogPasswordTitle"
    const val DialogPasswordErrorText = "DialogPasswordErrorText"
    const val DialogPasswordEditText = "DialogPasswordEditText"
  }
}