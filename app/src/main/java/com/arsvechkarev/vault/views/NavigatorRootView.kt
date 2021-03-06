package com.arsvechkarev.vault.views

import android.content.Context
import android.view.WindowInsets
import android.widget.FrameLayout

class NavigatorRootView(context: Context) : FrameLayout(context) {
  
  init {
    fitsSystemWindows = true
  }
  
  override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
    statusBarHeightFromInsets = insets.systemWindowInsetTop
    return super.onApplyWindowInsets(
      insets.replaceSystemWindowInsets(0, 0, 0,
        insets.systemWindowInsetBottom)
    )
  }
  
  companion object {
    
    var statusBarHeightFromInsets = 0
      private set
  }
}