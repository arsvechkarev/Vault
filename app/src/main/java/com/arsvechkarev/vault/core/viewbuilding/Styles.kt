package com.arsvechkarev.vault.core.viewbuilding

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.text.TextUtils
import android.view.Gravity
import android.widget.TextView
import com.arsvechkarev.vault.core.viewdsl.Ints.dp
import com.arsvechkarev.vault.core.viewdsl.background
import com.arsvechkarev.vault.core.viewdsl.font
import com.arsvechkarev.vault.core.viewdsl.gravity
import com.arsvechkarev.vault.core.viewdsl.paddingHorizontal
import com.arsvechkarev.vault.core.viewdsl.paddingVertical
import com.arsvechkarev.vault.core.viewdsl.rippleBackground
import com.arsvechkarev.vault.core.viewdsl.textColor
import com.arsvechkarev.vault.core.viewdsl.textSize

@Suppress("FunctionName")
object Styles {
  
  val BaseTextView: TextView.() -> Unit = {
    textSize(TextSizes.H4)
    font(Fonts.SegoeUi)
    textColor(Colors.TextPrimary)
    ellipsize = TextUtils.TruncateAt.END
  }
  
  val BoldTextView: TextView.() -> Unit = {
    font(Fonts.SegoeUiBold)
    textColor(Colors.TextPrimary)
  }
  
  fun ClickableTextView(
    rippleColor: Int,
    backgroundColor: Int,
  ): TextView.() -> Unit = {
    textColor(Colors.TextPrimary)
    textSize(TextSizes.H4)
    font(Typeface.DEFAULT_BOLD)
    paddingVertical(6.dp)
    paddingHorizontal(12.dp)
    rippleBackground(rippleColor, backgroundColor, 4.dp)
  }
  
  fun ClickableButton(
    colorStart: Int = Colors.Accent,
    colorEnd: Int = Colors.AccentLight,
  ): TextView.() -> Unit = {
    apply(BoldTextView)
    val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.BL_TR, intArrayOf(
      colorStart, colorEnd))
    val r = 120.dp.toFloat()
    val outerRadii = floatArrayOf(r, r, r, r, r, r, r, r)
    gradientDrawable.cornerRadii = outerRadii
    val roundRectShape = RoundRectShape(outerRadii, null, null)
    val maskRect = ShapeDrawable().apply {
      shape = roundRectShape
      paint.color = Colors.Ripple
    }
    val colorStateList = ColorStateList.valueOf(Colors.Ripple)
    background(RippleDrawable(colorStateList, gradientDrawable, maskRect))
    paddingVertical(6.dp)
    paddingHorizontal(20.dp)
    textSize(TextSizes.H3)
    gravity(Gravity.CENTER)
    isClickable = true
    isFocusable = true
  }
}