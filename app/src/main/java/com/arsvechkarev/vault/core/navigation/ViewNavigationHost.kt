package com.arsvechkarev.vault.core.navigation

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT

/**
 * Implementation that takes view is as root
 */
class ViewNavigationHost(
  private val root: ViewGroup,
  private val screenHandlerViewProvider: (ScreenHandler) -> View
) : NavigationHost {
  
  override fun isAttached(handler: ScreenHandler): Boolean {
    return screenHandlerViewProvider(handler).isAttachedToWindow
  }
  
  override fun addScreenHandler(handler: ScreenHandler) {
    val view = screenHandlerViewProvider(handler)
    root.addView(view, MATCH_PARENT, MATCH_PARENT)
  }
  
  override fun removeScreenHandler(handler: ScreenHandler) {
    root.removeView(screenHandlerViewProvider(handler))
  }
  
  override fun removeScreenHandlerAfterDelay(
    handler: ScreenHandler,
    animationDelay: Long,
    rightBeforeRemoving: () -> Unit) {
    root.postDelayed({
      root.removeView(screenHandlerViewProvider(handler))
    }, animationDelay)
  }
  
  override fun removeAllScreenHandlers() {
    root.removeAllViews()
  }
}