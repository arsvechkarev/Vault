package com.arsvechkarev.vault.viewbuilding

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.SwitchCompat
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.TypefaceSpan
import com.arsvechkarev.vault.core.views.drawables.GradientBackgroundDrawable
import com.arsvechkarev.vault.viewbuilding.Dimens.CornerRadiusSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginMedium
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import viewdsl.DefaultStyles
import viewdsl.Style
import viewdsl.background
import viewdsl.circleRippleBackground
import viewdsl.dp
import viewdsl.font
import viewdsl.gravity
import viewdsl.image
import viewdsl.padding
import viewdsl.paddingHorizontal
import viewdsl.paddingVertical
import viewdsl.rippleBackground
import viewdsl.setColors
import viewdsl.textColor
import viewdsl.textSize

object Styles : DefaultStyles {
  
  override val BaseRootBackground: Style<View> = {
    background = GradientBackgroundDrawable(context)
    isClickable = true
  }
  
  val BaseTextView: TextView.() -> Unit = {
    textSize(TextSizes.H6)
    font(Fonts.SegoeUi)
    textColor(Colors.TextPrimary)
    ellipsize = TextUtils.TruncateAt.END
  }
  
  val SecondaryTextView: TextView.() -> Unit = {
    apply(BaseTextView)
    textColor(Colors.TextSecondary)
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
    textSize(TextSizes.H6)
    font(Typeface.DEFAULT_BOLD)
    paddingVertical(MarginSmall)
    textColor(Colors.AccentLight)
    paddingHorizontal(MarginMedium)
    rippleBackground(rippleColor, Colors.Transparent, CornerRadiusSmall)
  }
  
  val ClickableErrorTextView: TextView.() -> Unit = {
    apply(ClickableTextView(Colors.ErrorRipple))
    textColor(Colors.TextError)
  }
  
  fun ClickableGradientRoundRect(
    colorStart: Int = Colors.Accent,
    colorEnd: Int = Colors.AccentLight,
  ): View.() -> Unit = {
    val gradientDrawable = GradientDrawable(
      GradientDrawable.Orientation.TL_BR, intArrayOf(colorStart, colorEnd)
    )
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
  }
  
  fun Button(
    colorStart: Int = Colors.Accent,
    colorEnd: Int = Colors.AccentLight,
  ): TextView.() -> Unit = {
    apply(BoldTextView)
    apply(ClickableGradientRoundRect(colorStart, colorEnd))
    padding(MarginMedium)
    textSize(TextSizes.H4)
    gravity(Gravity.CENTER)
    isClickable = true
    isFocusable = true
  }
  
  val AccentTextView: TextView.() -> Unit = {
    apply(BaseTextView)
    textColor(Colors.Accent)
    textSize(TextSizes.H6)
  }
  
  val ImageBack: ImageView.() -> Unit = {
    image(R.drawable.ic_back)
    padding(Dimens.IconPadding)
    circleRippleBackground(rippleColor = Colors.Ripple)
  }
  
  val ImageCross: ImageView.() -> Unit = {
    apply(ImageBack)
    image(R.drawable.ic_cross)
  }
  
  fun BaseEditText(@StringRes hint: Int = 0): EditText.() -> Unit = {
    textSize(TextSizes.H4)
    setSingleLine()
    background = null
    paddingHorizontal(0)
    paddingVertical(MarginSmall)
    imeOptions = EditorInfo.IME_ACTION_DONE
    setHintTextColor(Colors.TextSecondary)
    if (hint != 0) {
      val spannableString = SpannableString(context.getString(hint))
      spannableString.setSpan(
        TypefaceSpan(Fonts.SegoeUi), 0, spannableString.length,
        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
      )
      setHint(spannableString)
    }
  }
  
  val Switch: SwitchCompat.() -> Unit = {
    setColors(
      colorThumbEnabled = Colors.Accent,
      colorThumbDisabled = Colors.SwitchThumbUnchecked,
      colorTrackEnabled = Colors.AccentHeavy,
      colorTrackDisabled = Colors.SwitchTrackUnchecked,
    )
  }
  
  val CircleCheckmarkButton: ImageView.() -> Unit = {
    padding(MarginMedium)
    image(R.drawable.ic_checmark)
    circleRippleBackground(
      rippleColor = Colors.Ripple,
      backgroundColor = Colors.Accent
    )
  }
}
