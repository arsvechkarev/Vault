package navigation

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT

/**
 * Implementation that takes view as a root
 */
class ViewNavigationHost(
  private val root: ViewGroup,
  private val screenHandlerViewProvider: (ScreenHandler) -> View?
) : NavigationHost {
  
  override fun isAttached(handler: ScreenHandler): Boolean {
    return screenHandlerViewProvider(handler)?.isAttachedToWindow == true
  }
  
  override fun addScreenHandler(handler: ScreenHandler) {
    val view = screenHandlerViewProvider(handler)
    root.addView(view, MATCH_PARENT, MATCH_PARENT)
  }
  
  override fun removeScreenHandler(handler: ScreenHandler) {
    root.removeView(screenHandlerViewProvider(handler))
  }
  
  override fun removeScreenHandlerAfterDelay(handler: ScreenHandler, delay: Long, afterDelay: () -> Unit) {
    root.postDelayed({
      val view = screenHandlerViewProvider(handler)
      if (view?.isAttachedToWindow == true) {
        root.removeView(view)
      }
      afterDelay()
    }, delay)
  }
  
  override fun removeAllScreenHandlers() {
    root.removeAllViews()
  }
}