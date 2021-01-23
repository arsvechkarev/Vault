package com.arsvechkarev.vault.views.behaviors

import android.view.View
import androidx.core.view.ViewCompat

class HeaderBehaviorOffsetHelper(
  val parent: View,
  val view: View,
  var slideRangeCoefficient: Float
) {
  
  var topAndBottomOffset = 0
    private set
  
  val maxScrollingRange: Int
    get() = (-view.height * slideRangeCoefficient).toInt()
  
  fun updateOffset(dy: Int): Int {
    val prefOffset = topAndBottomOffset
    if (dy != 0) {
      val resultOffset = (topAndBottomOffset - dy).coerceIn(maxScrollingRange, 0)
      topAndBottomOffset = resultOffset
      ViewCompat.offsetTopAndBottom(view, (topAndBottomOffset - view.top) + parent.paddingTop)
    }
    return prefOffset - topAndBottomOffset
  }
  
}