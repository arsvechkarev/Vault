package navigation

import android.os.Bundle

/**
 * Handles interactions with the Screen
 */
interface ScreenHandler {
  
  /**
   * Duration of screen's hide animation. Navigator uses this to remove screen only after particular
   * period of time
   */
  val hideAnimationDuration: Long get() = 0
  
  /**
   * Sets arguments for this screen
   */
  fun setupArguments(bundle: Bundle)
  
  /**
   * Creates view of this screen if view is null already
   */
  fun buildViewIfNeeded()
  
  /**
   * Performs screen initialization. See [Screen.onInit]
   */
  fun performInit()
  
  /**
   * Shows screen with animation type [type]
   */
  fun performShow(type: AnimationType)
  
  /**
   * Hides screen with animation type [type]. May not be called when, for example, user presses
   * back and current screen is the only screen in stack. In this case just [performFullRelease] is
   * called
   */
  fun performHide(type: AnimationType)
  
  /**
   * Removes view, but not removes other screen metadata, For example, if you are using moxy,
   * this would be time to call MvpDelegate.onDestroyView() and MvpDelegate.onDetach(), so that
   * presenter will be saved properly
   */
  fun performOnlyViewRemoval()
  
  /**
   * Releases screen completely and clears all associated data. For example, if you are using moxy,
   * this would be time to call MvpDelegate.onDestroy()
   */
  fun performFullRelease()
  
  /**
   * Saves instance state for screen with bundle
   */
  fun performSaveInstanceState(bundle: Bundle)
  
  /**
   * See [Screen.handleBackPress]
   */
  fun handleBackPress(): Boolean
  
  /**
   * Notifies screen that orientation became portrait
   */
  fun onOrientationBecamePortrait()
  
  /**
   * Notifies screen that orientation became landscape
   */
  fun onOrientationBecameLandscape()
}