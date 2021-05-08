package com.arsvechkarev.vault.viewdsl

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.arsvechkarev.vault.views.RootView
import java.util.Locale

fun dimen(dimenRes: Int) = ContextHolder.applicationContext.resources.getDimension(dimenRes)

val isOrientationPortrait: Boolean
  get() = ContextHolder.applicationContext.resources.configuration.orientation ==
      Configuration.ORIENTATION_PORTRAIT

val isLayoutLeftToRight: Boolean
  get() = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_LTR

val Context.screenWidth: Int
  get() = resources.displayMetrics.widthPixels

val Context.screenHeight: Int
  get() = resources.displayMetrics.heightPixels

val Context.statusBarHeight: Int
  get() {
    if (RootView.statusBarHeightFromInsets != 0) {
      return RootView.statusBarHeightFromInsets
    }
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
      result = resources.getDimensionPixelSize(resourceId)
    }
    return result
  }

fun Context.showKeyboard() {
  val inputMethodManager =
      getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
  inputMethodManager!!.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

fun Context.setSoftInputMode(mode: Int) {
  (this as Activity).window.setSoftInputMode(mode)
}

fun Context.hideKeyboard() {
  val inputMethodManager =
      getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
  val token = (this as? Activity)?.window?.decorView?.windowToken ?: View(this).windowToken
  inputMethodManager!!.hideSoftInputFromWindow(token, 0)
}

@ColorInt
fun Context.getAttrColor(@AttrRes resId: Int): Int {
  val typedValue = TypedValue()
  val resolved = theme.resolveAttribute(resId, typedValue, true)
  require(resolved) { "Attribute cannot be resolved" }
  return typedValue.data
}

fun Context.retrieveDrawable(@DrawableRes resId: Int): Drawable {
  return ContextCompat.getDrawable(this, resId)!!
}

inline fun <reified T> Context.getSystemService(): T {
  return ContextCompat.getSystemService(this, T::class.java)!!
}

fun Context.createLayoutParams(
  width: Size,
  height: Size,
  margins: Margins = Margins()
): ViewGroup.LayoutParams {
  val widthValue: Int = determineSize(this, width)
  val heightValue: Int = determineSize(this, height)
  val layoutParams = ViewGroup.MarginLayoutParams(widthValue, heightValue)
  layoutParams.setMargins(
    margins.left,
    margins.top,
    margins.right,
    margins.bottom
  )
  return layoutParams
}

fun determineSize(context: Context, size: Size) = when (size) {
  Size.MATCH_PARENT -> ViewGroup.LayoutParams.MATCH_PARENT
  Size.WRAP_CONTENT -> ViewGroup.LayoutParams.WRAP_CONTENT
  is Size.IntSize -> size.size
  is Size.Dimen -> context.resources.getDimension(size.dimenRes).toInt()
}
