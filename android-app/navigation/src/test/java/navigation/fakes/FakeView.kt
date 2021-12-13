package navigation.fakes

class FakeView {
  var isAttachedToWindow = false
  var isVisible = true
  var tag: Any? = null
  
  override fun toString(): String {
    return "FakeView(isAttachedToWindow=$isAttachedToWindow, isVisible=$isVisible)"
  }
}