package com.arsvechkarev.vault.viewbuilding

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewbuilding.Dimens.CornerRadiusSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginMedium
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewdsl.background
import com.arsvechkarev.vault.viewdsl.circleRippleBackground
import com.arsvechkarev.vault.viewdsl.dp
import com.arsvechkarev.vault.viewdsl.font
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.image
import com.arsvechkarev.vault.viewdsl.padding
import com.arsvechkarev.vault.viewdsl.paddingHorizontal
import com.arsvechkarev.vault.viewdsl.paddingVertical
import com.arsvechkarev.vault.viewdsl.rippleBackground
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.views.drawables.GradientBackgroundDrawable

@Suppress("FunctionName")
object Styles {
  
  val BaseBackground: View.() -> Unit = {
    background = GradientBackgroundDrawable(context)
  }
  
  val BaseTextView: TextView.() -> Unit = {
    textSize(TextSizes.H5)
    font(Fonts.SegoeUi)
    textColor(Colors.TextPrimary)
    ellipsize = TextUtils.TruncateAt.END
  }
  
  val BoldTextView: TextView.() -> Unit = {
    apply(BaseTextView)
    font(Fonts.SegoeUiBold)
    textSize(TextSizes.H4)
  }
  
  val TitleTextView: TextView.() -> Unit = {
    apply(BoldTextView)
    textSize(TextSizes.H0)
  }
  
  fun ClickableTextView(
    rippleColor: Int = Colors.AccentRipple,
  ): TextView.() -> Unit = {
    textSize(TextSizes.H5)
    font(Typeface.DEFAULT_BOLD)
    paddingVertical(MarginSmall)
    textColor(Colors.AccentLight)
    paddingHorizontal(MarginNormal)
    rippleBackground(rippleColor, Colors.Transparent, CornerRadiusSmall)
  }
  
  val ClickableErrorTextView: TextView.() -> Unit = {
    apply(ClickableTextView(Colors.ErrorRipple))
    textColor(Colors.Error)
  }
  
  fun Button(
    colorStart: Int = Colors.Accent,
    colorEnd: Int = Colors.AccentLight,
  ): TextView.() -> Unit = {
    apply(BoldTextView)
    val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(
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
    padding(MarginMedium)
    textSize(TextSizes.H4)
    gravity(Gravity.CENTER)
    isClickable = true
    isFocusable = true
  }
  
  val ImageBack: ImageView.() -> Unit = {
    image(R.drawable.ic_back)
    padding(Dimens.IconPadding)
    circleRippleBackground()
  }
  
  val BaseEditText: EditText.() -> Unit = {
    font(Fonts.SegoeUi)
    textSize(TextSizes.H3)
    paddingVertical(MarginSmall)
    setSingleLine()
    inputType = TYPE_TEXT_VARIATION_PASSWORD
    imeOptions = EditorInfo.IME_ACTION_DONE
  }
}