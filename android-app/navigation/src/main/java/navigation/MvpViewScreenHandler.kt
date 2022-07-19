package navigation

import android.content.Context
import android.os.Bundle
import android.view.View
import moxy.MvpDelegate
import navigation.AnimationType.BACKWARD
import navigation.AnimationType.FORWARD
import navigation.AnimationType.NONE

class MvpViewScreenHandler(
  private val screen: BaseScreen,
  private val screenKey: String,
  private val parentMvpDelegate: MvpDelegate<*>,
  private val context: Context
) : ScreenHandler {
  
  private var _bundle: Bundle = Bundle.EMPTY
  
  private var isParentDelegateSet = false
  
  val bundle: Bundle get() = _bundle
  
  val view: View? get() = screen._view
  
  override fun setupArguments(bundle: Bundle) {
    screen._arguments = bundle
  }
  
  override fun buildViewIfNeeded() {
    if (screen._view == null) {
      screen._view = screen.buildLayout(context)
    }
  }
  
  override fun performInit() {
    if (!isParentDelegateSet) {
      screen.setParentDelegate(parentMvpDelegate, screenKey)
      isParentDelegateSet = true
    }
    screen.mvpDelegate.onCreate()
    screen.onInit()
  }
  
  override fun performShow(type: AnimationType) {
    val view = view ?: error("View is null in performShow()")
    screen.mvpDelegate.onAttach()
    val endAction = { if (screen._view != null) screen.onAppearedOnScreenAfterAnimation() }
    when (type) {
      NONE -> view.visibility = View.VISIBLE
      FORWARD -> view.animate(NavigationConfig.animations::AppearanceAsGoingForward, endAction)
      BACKWARD -> view.animate(NavigationConfig.animations::AppearanceAsGoingBackward, endAction)
    }
    screen.onAppearedOnScreen()
    if (type != BACKWARD) {
      screen.onAppearedOnScreenGoingForward()
    }
  }
  
  override fun performHide(type: AnimationType, doOnEnd: () -> Unit) {
    val view = view ?: error("View is null in performHide()")
    screen.mvpDelegate.onDetach()
    val endAction = {
      if (screen._view != null) {
        screen.onDisappearedFromScreen()
      }
      doOnEnd()
    }
    when (type) {
      NONE -> view.visibility = View.GONE
      FORWARD -> view.animate(NavigationConfig.animations::DisappearanceAsGoingForward, endAction)
      BACKWARD -> view.animate(NavigationConfig.animations::DisappearanceAsGoingBackward, endAction)
    }
  }
  
  override fun performSaveInstanceState(bundle: Bundle) {
    screen.mvpDelegate.onSaveInstanceState(bundle)
  }
  
  override fun performOnlyViewRemoval() {
    screen.mvpDelegate.onDestroyView()
    screen.mvpDelegate.onDetach()
    releaseScreen()
  }
  
  override fun performFullRelease() {
    screen.mvpDelegate.onDestroyView()
    screen.mvpDelegate.onDetach()
    screen.mvpDelegate.onDestroy()
    releaseScreen()
  }
  
  override fun handleBackPress(): Boolean {
    return screen.handleBackPress()
  }
  
  override fun onOrientationBecamePortrait() {
    screen.onOrientationBecamePortrait()
  }
  
  override fun onOrientationBecameLandscape() {
    screen.onOrientationBecameLandscape()
  }
  
  private fun releaseScreen() {
    screen.onRelease()
    screen._view = null
    screen.clearViewCache()
  }
  
  private fun View.animate(
    animation: (doOnEnd: () -> Unit) -> View.() -> Unit,
    doOnEnd: () -> Unit
  ) {
    apply(animation(doOnEnd))
  }
}