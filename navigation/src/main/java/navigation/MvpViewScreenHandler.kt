package navigation

import android.content.Context
import android.os.Bundle
import android.view.View
import config.DurationsConfigurator
import moxy.MvpDelegate
import navigation.AnimationType.ANIMATION_BACKWARD
import navigation.AnimationType.ANIMATION_FORWARD
import navigation.AnimationType.ANIMATION_NONE

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
  
  override val hideAnimationDuration = DurationsConfigurator.DurationShort
  
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
    when (type) {
      ANIMATION_NONE -> view.visibility == View.VISIBLE
      ANIMATION_FORWARD -> view.apply(appearanceAsGoingForward)
      ANIMATION_BACKWARD -> view.apply(appearanceAsGoingBackward)
    }
    screen.onAppearedOnScreen()
  }
  
  override fun performHide(type: AnimationType) {
    val view = view ?: error("View is null in performHide()")
    screen.mvpDelegate.onDetach()
    when (type) {
      ANIMATION_NONE -> view.visibility = View.GONE
      ANIMATION_FORWARD -> view.apply(disappearanceAsGoingForward)
      ANIMATION_BACKWARD -> view.apply(disappearanceAsGoingBackward)
    }
    screen.onDisappearedFromScreen()
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
  
  private companion object {
    
    private val appearanceAsGoingForward: View.() -> Unit = {
      animateSlideFromRight(duration = DurationsConfigurator.DurationShort)
    }
    
    private val appearanceAsGoingBackward: View.() -> Unit = {
      animateVisible(duration = DurationsConfigurator.DurationShort)
    }
    
    private val disappearanceAsGoingForward: View.() -> Unit = {
      animateGoneAfter(duration = DurationsConfigurator.DurationShort)
    }
    
    private val disappearanceAsGoingBackward: View.() -> Unit = {
      animateSlideToRight(duration = DurationsConfigurator.DurationShort)
    }
  }
}