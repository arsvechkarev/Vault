package com.arsvechkarev.vault.viewbuilding

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.text.InputType
import android.text.TextUtils
import android.view.Gravity
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageBackMargin
import com.arsvechkarev.vault.viewdsl.Ints.dp
import com.arsvechkarev.vault.viewdsl.background
import com.arsvechkarev.vault.viewdsl.circleRippleBackground
import com.arsvechkarev.vault.viewdsl.font
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.image
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.padding
import com.arsvechkarev.vault.viewdsl.paddingHorizontal
import com.arsvechkarev.vault.viewdsl.paddingVertical
import com.arsvechkarev.vault.viewdsl.rippleBackground
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize

@Suppress("FunctionName")
object Styles {
  
  val BaseTextView: TextView.() -> Unit = {
    textSize(TextSizes.H4)
    font(Fonts.SegoeUi)
    textColor(Colors.TextPrimary)
    ellipsize = TextUtils.TruncateAt.END
  }
  
  val BoldTextView: TextView.() -> Unit = {
    apply(BaseTextView)
    font(Fonts.SegoeUiBold)
    textSize(TextSizes.H3)
  }
  
  fun ClickableTextView(
    rippleColor: Int = Colors.AccentRipple,
  ): TextView.() -> Unit = {
    textColor(Colors.TextPrimary)
    textSize(TextSizes.H4)
    font(Typeface.DEFAULT_BOLD)
    paddingVertical(6.dp)
    textColor(Colors.AccentLight)
    paddingHorizontal(12.dp)
    rippleBackground(rippleColor, Colors.Transparent, 4.dp)
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
  
  val ImageBack: ImageView.() -> Unit = {
    image(R.drawable.ic_back)
    margins(top = ImageBackMargin, start = ImageBackMargin)
    padding(Dimens.IconPadding)
    circleRippleBackground()
  }
  
  val BaseEditText: EditText.() -> Unit = {
    font(Fonts.SegoeUi)
    textSize(TextSizes.H3)
    paddingVertical(8.dp)
    setSingleLine()
    inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
  }
}