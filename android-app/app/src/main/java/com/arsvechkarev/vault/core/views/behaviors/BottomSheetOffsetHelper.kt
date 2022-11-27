package com.arsvechkarev.vault.core.views.behaviors

import android.view.View

class BottomSheetOffsetHelper(
  private val bottomSheet: View,
  private val onTopChanged: (Int) -> Unit = {}
) {
  
  private var parentHeight = 0
  private var layoutTop: Int = 0
  
  fun onViewLayout(parentHeight: Int) {
    this.parentHeight = parentHeight
    layoutTop = bottomSheet.top
  }
  
  fun updateDyOffset(offset: Int): Int {
    val newTop = bottomSheet.top + offset
    val oldTop = bottomSheet.top
    bottomSheet.top = newTop.coerceIn(layoutTop, parentHeight).also(onTopChanged)
    return bottomSheet.top - oldTop
  }
  
  fun updateTop(value: Int) {
    bottomSheet.top = value.coerceIn(layoutTop, parentHeight).also(onTopChanged)
  }
}
