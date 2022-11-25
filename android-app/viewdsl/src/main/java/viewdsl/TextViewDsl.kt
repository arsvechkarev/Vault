@file:Suppress("NOTHING_TO_INLINE")

package viewdsl

import android.graphics.Color
import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

fun TextView.drawables(
  @DrawableRes start: Int = 0,
  @DrawableRes top: Int = 0,
  @DrawableRes end: Int = 0,
  @DrawableRes bottom: Int = 0,
  color: Int = Color.TRANSPARENT
) {
  val drawableStart = if (start != 0) context.retrieveDrawable(start) else null
  val drawableTop = if (top != 0) context.retrieveDrawable(top) else null
  val drawableEnd = if (end != 0) context.retrieveDrawable(end) else null
  val drawableBottom = if (bottom != 0) context.retrieveDrawable(bottom) else null
  drawableStart?.apply { colorFilter = PorterDuffColorFilter(color, SRC_ATOP) }
  drawableTop?.apply { colorFilter = PorterDuffColorFilter(color, SRC_ATOP) }
  drawableEnd?.apply { colorFilter = PorterDuffColorFilter(color, SRC_ATOP) }
  drawableBottom?.apply { colorFilter = PorterDuffColorFilter(color, SRC_ATOP) }
  if (isLayoutLeftToRight) {
    setCompoundDrawablesWithIntrinsicBounds(drawableStart, drawableTop, drawableEnd, drawableBottom)
  } else {
    setCompoundDrawablesWithIntrinsicBounds(drawableEnd, drawableTop, drawableStart, drawableBottom)
  }
}

fun TextView.clearCompoundDrawables() {
  setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
}

inline fun TextView.textSize(size: Float) {
  setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
}

inline fun TextView.text(@StringRes resId: Int) {
  setText(resId)
}

inline fun TextView.text(text: CharSequence) {
  setText(text)
}

inline fun TextView.clearText() {
  text = null
}

inline fun TextView.textColor(color: Int) {
  setTextColor(color)
}

inline fun TextView.drawablePadding(padding: Int) {
  compoundDrawablePadding = padding
}

inline fun TextView.font(font: Typeface) {
  typeface = font
}

inline fun TextView.gravity(gravity: Int) {
  this.gravity = gravity
}
