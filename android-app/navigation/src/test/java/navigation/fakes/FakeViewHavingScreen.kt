package navigation.fakes

import navigation.Screen

abstract class FakeViewHavingScreen : Screen {
  
  override var arguments: Map<String, Any> = HashMap()
  
  var state: LifecycleState = LifecycleState.NONE
    private set
  
  fun buildView(): FakeView = FakeView()
  
  override fun onInit() {
    state = LifecycleState.INITIALIZED
  }
  
  override fun onAppearedOnScreen() {
    state = LifecycleState.VISIBLE
  }
  
  override fun onDisappearedFromScreen() {
    state = LifecycleState.NOT_VISIBLE
  }
  
  override fun onRelease() {
    state = LifecycleState.NONE
  }
}