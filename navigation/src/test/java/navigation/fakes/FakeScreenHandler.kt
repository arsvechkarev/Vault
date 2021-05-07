package navigation.fakes

import android.os.Bundle
import navigation.AnimationType
import navigation.Screen
import navigation.ScreenHandler
import navigation.getViewTag

class FakeScreenHandler(private val fakeScreen: FakeViewHavingScreen) : ScreenHandler {
  
  private var arguments: Map<String, Any> = HashMap()
  
  var fakeView: FakeView? = null
    private set
  
  val screen: Screen get() = fakeScreen
  
  override val hideAnimationDuration: Long = 400
  
  override fun setupArguments(bundle: Bundle) {
  }
  
  override fun buildViewIfNeeded() {
    if (fakeView == null) {
      fakeView = fakeScreen.buildView()
      fakeView!!.tag = fakeScreen::class.getViewTag()
    }
  }
  
  override fun performInit() {
    fakeView!!.isAttachedToWindow = true
    fakeScreen.onInit()
  }
  
  override fun performShow(type: AnimationType) {
    fakeView!!.isVisible = true
    fakeScreen.onAppearedOnScreen()
  }
  
  override fun performHide(type: AnimationType) {
    fakeView!!.isVisible = false
    fakeScreen.onDisappearedFromScreen()
  }
  
  override fun performOnlyViewRemoval() {
  }
  
  override fun performFullRelease() {
    fakeView!!.isAttachedToWindow = false
    fakeScreen.onRelease()
  }
  
  override fun performSaveInstanceState(bundle: Bundle) {
  }
  
  override fun handleBackPress(): Boolean {
    return true
  }
  
  override fun onOrientationBecamePortrait() {
    fakeScreen.onOrientationBecamePortrait()
  }
  
  override fun onOrientationBecameLandscape() {
    fakeScreen.onOrientationBecameLandscape()
  }
}