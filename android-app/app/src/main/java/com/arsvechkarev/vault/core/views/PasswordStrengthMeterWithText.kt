package com.arsvechkarev.vault.core.views

import android.content.Context
import android.widget.FrameLayout
import android.widget.TextView
import buisnesslogic.PasswordStrength
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.PasswordStrengthMeterHeight
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.IntSize
import viewdsl.exactly
import viewdsl.layoutLeftTop
import viewdsl.margin
import viewdsl.margins
import viewdsl.size
import viewdsl.text
import viewdsl.textSize
import viewdsl.unspecified
import viewdsl.withViewBuilder

class PasswordStrengthMeterWithText(context: Context) : FrameLayout(context) {
  
  private val textView get() = getChildAt(0) as TextView
  private val passwordStrengthMeter get() = getChildAt(1) as PasswordStrengthMeter
  
  init {
    withViewBuilder {
      child<FixedSizeTextView>(WrapContent, WrapContent, style = BoldTextView) {
        exampleTextRes = R.string.text_medium
        margins(start = MarginNormal, top = MarginLarge)
        textSize(TextSizes.H3)
      }
      child<PasswordStrengthMeter>(MatchParent, IntSize(PasswordStrengthMeterHeight)) {
        margin(MarginNormal)
      }
    }
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val textMaxWidth = textView.paint.measureText(context.getString(R.string.text_medium))
    val widthSize = widthMeasureSpec.size
    textView.measure(unspecified(), unspecified())
    val passwordMeterWidth = exactly(widthSize - textMaxWidth.toInt() - MarginNormal)
    val passwordMeterHeight = exactly(PasswordStrengthMeterHeight)
    passwordStrengthMeter.measure(passwordMeterWidth, passwordMeterHeight)
    setMeasuredDimension(
      resolveSize(widthSize, widthMeasureSpec),
      resolveSize(textView.measuredHeight, heightMeasureSpec),
    )
  }
  
  fun setText(textResId: Int) {
    textView.text(textResId)
  }
  
  fun setStrength(strength: PasswordStrength?) {
    passwordStrengthMeter.setStrength(strength)
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    val textMaxWidth = textView.paint.measureText(context.getString(R.string.text_medium)).toInt()
    textView.layoutLeftTop(textMaxWidth / 2 - textView.measuredWidth / 2, 0)
    passwordStrengthMeter.layoutLeftTop(textMaxWidth + MarginNormal,
      height / 2 - passwordStrengthMeter.height / 2)
  }
}
