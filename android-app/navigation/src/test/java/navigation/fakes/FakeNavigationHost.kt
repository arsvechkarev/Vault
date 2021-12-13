package navigation.fakes

import navigation.NavigationHost
import navigation.ScreenHandler

class FakeNavigationHost(
  private val viewProvider: (ScreenHandler) -> FakeView
) : NavigationHost {
  
  val views = ArrayList<FakeView>()
  
  override fun isAttached(handler: ScreenHandler): Boolean {
    return (handler as FakeScreenHandler).fakeView!!.isAttachedToWindow
  }
  
  override fun addScreenHandler(handler: ScreenHandler) {
    views.add(viewProvider.invoke(handler))
  }
  
  override fun removeScreenHandler(handler: ScreenHandler) {
    views.remove(viewProvider.invoke(handler))
  }
  
  override fun removeScreenHandlerAfterDelay(handler: ScreenHandler, delay: Long, afterDelay: () -> Unit) {
    Thread.sleep(delay)
    views.remove(viewProvider.invoke(handler))
  }
  
  override fun removeAllScreenHandlers() {
    views.clear()
  }
}