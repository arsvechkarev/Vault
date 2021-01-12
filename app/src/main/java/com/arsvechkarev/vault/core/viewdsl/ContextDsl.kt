package com.arsvechkarev.vault.core.viewdsl

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.arsvechkarev.viewdsl.Size
import java.util.Locale

fun dimen(dimenRes: Int) = ContextHolder.context.resources.getDimension(dimenRes)

val isOrientationPortrait: Boolean
  get() = ContextHolder.context.resources.configuration.orientation ==
      Configuration.ORIENTATION_PORTRAIT

val isLayoutLeftToRight: Boolean
  get() = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_LTR

val Context.statusBarHeight: Int
  get() {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
      result = resources.getDimensionPixelSize(resourceId)
    }
    return result
  }

@ColorInt
fun Context.getAttrColor(@AttrRes resId: Int): Int {
  val typedValue = TypedValue()
  val resolved = theme.resolveAttribute(resId, typedValue, true)
  require(resolved) { "Attribute cannot be resolved" }
  return typedValue.data
}

fun Context.retrieveDrawable(@DrawableRes resId: Int): Drawable {
  return ContextCompat.getDrawable(this, resId)!!.mutate()
}

fun Context.createLayoutParams(
  width: Size,
  height: Size,
  margins: Margins = Margins()
): ViewGroup.LayoutParams {
  val widthValue: Int = determineSize(width)
  val heightValue: Int = determineSize(height)
  val layoutParams = ViewGroup.MarginLayoutParams(widthValue, heightValue)
  layoutParams.setMargins(
    margins.left,
    margins.top,
    margins.right,
    margins.bottom
  )
  return layoutParams
}

fun Context.determineSize(size: Size) = when (size) {
  Size.MATCH_PARENT -> ViewGroup.LayoutParams.MATCH_PARENT
  Size.WRAP_CONTENT -> ViewGroup.LayoutParams.WRAP_CONTENT
  is Size.IntSize -> size.size
  is Size.Dimen -> resources.getDimension(size.dimenRes).toInt()
}
