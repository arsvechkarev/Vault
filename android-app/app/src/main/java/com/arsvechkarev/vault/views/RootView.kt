package com.arsvechkarev.vault.views

import android.content.Context
import android.view.WindowInsets
import android.widget.FrameLayout
import viewdsl.ViewDslConfiguration

class RootView(context: Context) : FrameLayout(context) {
  
  init {
    fitsSystemWindows = true
  }
  
  override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
    ViewDslConfiguration.setStatusBarHeight(insets.systemWindowInsetTop)
    return super.onApplyWindowInsets(
      insets.replaceSystemWindowInsets(0, 0, 0,
        insets.systemWindowInsetBottom)
    )
  }
}
