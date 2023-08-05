package viewdsl

import android.view.View
import android.view.View.MeasureSpec.UNSPECIFIED

fun exactly(size: Int): Int {
  return View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY)
}

fun atMost(size: Int): Int {
  return View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.AT_MOST)
}

fun unspecified(size: Int = 0): Int {
  return View.MeasureSpec.makeMeasureSpec(size, UNSPECIFIED)
}

val Int.size: Int
  get() = View.MeasureSpec.getSize(this)

val Int.mode: Int
  get() = View.MeasureSpec.getMode(this)