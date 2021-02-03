package com.arsvechkarev.vault.views

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.cryptography.PasswordStrength
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens
import com.arsvechkarev.vault.viewbuilding.Dimens.IconSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.PasswordStrengthMeterHeight
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.Styles.BaseEditText
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.Size.IntSize
import com.arsvechkarev.vault.viewdsl.addView
import com.arsvechkarev.vault.viewdsl.circleRippleBackground
import com.arsvechkarev.vault.viewdsl.exactly
import com.arsvechkarev.vault.viewdsl.font
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.heightWithMargins
import com.arsvechkarev.vault.viewdsl.image
import com.arsvechkarev.vault.viewdsl.invisible
import com.arsvechkarev.vault.viewdsl.layoutLeftTop
import com.arsvechkarev.vault.viewdsl.marginEnd
import com.arsvechkarev.vault.viewdsl.marginStart
import com.arsvechkarev.vault.viewdsl.marginTop
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.padding
import com.arsvechkarev.vault.viewdsl.size
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.viewdsl.unspecified
import com.arsvechkarev.vault.viewdsl.withViewBuilder

class EditablePasswordViewGroup(context: Context) : ViewGroup(context) {
  
  private val editText get() = getChildAt(0) as EditText
  private val textPassword get() = getChildAt(1) as TextView
  private val imageSave get() = getChildAt(2) as ImageView
  private val textError get() = getChildAt(3) as TextView
  private val textPasswordStrength get() = getChildAt(4) as TextView
  private val passwordStrengthMeter get() = getChildAt(5) as PasswordStrengthMeter
  
  var onSaveClicked: (newName: String) -> Unit = {}
  
  init {
    withViewBuilder {
      EditText(MatchParent, WrapContent, style = BaseEditText) {
        text("Password")
        gravity(Gravity.CENTER)
        textSize(TextSizes.H2)
        font(Fonts.SegoeUiBold)
        isSingleLine = false
      }
      TextView(WrapContent, WrapContent, style = BoldTextView) {
        gravity(Gravity.CENTER)
        textSize(TextSizes.H2)
        text("Password")
      }
      ImageView(WrapContent, WrapContent) {
        padding(Dimens.IconPadding)
        image(R.drawable.ic_checmark)
        circleRippleBackground()
        onClick { onSaveButtonClicked() }
      }
      TextView(WrapContent, WrapContent, style = BaseTextView) {
        gravity(Gravity.CENTER)
        margins(start = MarginDefault, top = MarginDefault)
        textColor(Colors.Error)
        textSize(TextSizes.H3)
        invisible()
        text("Something is wrong")
      }
      TextView(WrapContent, WrapContent, style = BaseTextView) {
        gravity(Gravity.CENTER)
        margins(start = MarginDefault, end = MarginDefault, top = MarginDefault)
        textSize(TextSizes.H2)
        text("Strength: MEDIUM")
      }
      addView {
        PasswordStrengthMeter(context).apply {
          size(MatchParent, IntSize(PasswordStrengthMeterHeight))
          margins(start = MarginDefault, end = MarginDefault, top = MarginSmall)
          setStrength(PasswordStrength.MEDIUM)
        }
      }
    }
  }
  
  fun goToEditMode() {
  
  }
  
  fun getEditTextString(): String {
    return editText.text.toString()
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val widthSize = widthMeasureSpec.size
    val maxTextAndPasswordWidth = widthSize - IconSize * 2 - MarginSmall * 2 - MarginDefault
    imageSave.measure(unspecified(), unspecified())
    textPassword.measure(exactly(maxTextAndPasswordWidth), unspecified())
    editText.measure(exactly(maxTextAndPasswordWidth), unspecified())
    textError.measure(unspecified(), unspecified())
    textPasswordStrength.measure(unspecified(), unspecified())
    val passwordStrengthMeterWidth = widthSize - passwordStrengthMeter.marginStart -
        passwordStrengthMeter.marginEnd
    passwordStrengthMeter.measure(exactly(passwordStrengthMeterWidth),
      exactly(PasswordStrengthMeterHeight))
    val maxTextHeight = maxOf(editText.measuredHeight,
      textPassword.measuredHeight, imageSave.measuredHeight)
    val height = maxTextHeight + MarginSmall * 3 + textError.heightWithMargins +
        textPasswordStrength.heightWithMargins + PasswordStrengthMeterHeight
    setMeasuredDimension(
      resolveSize(widthSize, widthMeasureSpec),
      resolveSize(height, heightMeasureSpec)
    )
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    val imageHeight = imageSave.measuredHeight
    val imageWidth = imageSave.measuredWidth
    val maxHeight = maxOf(editText.measuredHeight, textPassword.measuredHeight,
      imageSave.measuredHeight)
    textPassword.layoutLeftTop(width / 2 - textPassword.measuredWidth / 2,
      maxHeight / 2 - textPassword.measuredHeight / 2)
    editText.layoutLeftTop(width / 2 - editText.measuredWidth / 2,
      maxHeight / 2 - editText.measuredHeight / 2)
    imageSave.layoutLeftTop(width - imageWidth - MarginSmall,
      maxHeight / 2 - imageSave.height / 2)
    textError.layoutLeftTop(textError.marginStart, editText.bottom + textError.marginTop)
    textPasswordStrength.layoutLeftTop(textPasswordStrength.marginStart,
      textError.bottom + textPasswordStrength.marginTop)
    passwordStrengthMeter.layoutLeftTop(passwordStrengthMeter.marginStart,
      textPasswordStrength.bottom + passwordStrengthMeter.marginTop)
  }
  
  private fun onSaveButtonClicked() {
    val newPassword = editText.text.toString()
    if (newPassword.isBlank()) {
      return
    }
    onSaveClicked(newPassword)
  }
}