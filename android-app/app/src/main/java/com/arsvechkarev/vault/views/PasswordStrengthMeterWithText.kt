package com.arsvechkarev.vault.views

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
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.Size.IntSize
import com.arsvechkarev.vault.viewdsl.exactly
import com.arsvechkarev.vault.viewdsl.layoutLeftTop
import com.arsvechkarev.vault.viewdsl.margin
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.size
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.viewdsl.unspecified
import com.arsvechkarev.vault.viewdsl.withViewBuilder

class PasswordStrengthMeterWithText(context: Context) : FrameLayout(context) {
  
  private val textView get() = getChildAt(0) as TextView
  private val passwordStrengthMeter get() = getChildAt(1) as PasswordStrengthMeter
  
  init {
    withViewBuilder {
      TextView(WrapContent, WrapContent, style = BoldTextView) {
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