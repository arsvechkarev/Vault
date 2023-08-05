package viewdsl

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import java.util.Locale

val isOrientationPortrait: Boolean
  get() = ViewDslConfiguration.applicationContext.resources.configuration.orientation ==
      Configuration.ORIENTATION_PORTRAIT

val isLayoutLeftToRight: Boolean
  get() = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_LTR

val Context.screenWidth: Int
  get() = resources.displayMetrics.widthPixels

val Context.screenHeight: Int
  get() = resources.displayMetrics.heightPixels

val Context.statusBarHeight: Int
  get() {
    if (ViewDslConfiguration.statusBarHeight != 0) {
      return ViewDslConfiguration.statusBarHeight
    }
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
      result = resources.getDimensionPixelSize(resourceId)
    }
    return result
  }

fun Context.showKeyboard(editText: EditText? = null) {
  val inputMethodManager =
      getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
  val view = editText ?: (this as Activity).window.decorView
  inputMethodManager!!.showSoftInput(view, 0)
}

fun Context.hideKeyboard() {
  val inputMethodManager =
      getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  val token = (this as? Activity)?.window?.decorView?.windowToken ?: View(this).windowToken
  inputMethodManager.hideSoftInputFromWindow(token, 0)
}

fun Context.setSoftInputMode(mode: Int) {
  (this as Activity).window.setSoftInputMode(mode)
}

fun Context.retrieveDrawable(@DrawableRes resId: Int): Drawable {
  return ContextCompat.getDrawable(this, resId)!!
}

fun Context.getDrawableHeight(@DrawableRes resId: Int): Int {
  return retrieveDrawable(resId).intrinsicHeight
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
