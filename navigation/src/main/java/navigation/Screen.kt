package navigation

import android.os.Bundle

/**
 * Screen is interface for an application screen
 */
interface Screen {
  
  val arguments: Bundle
  
  /**
   * Called when screen is initialized the first time
   */
  fun onInit() = Unit
  
  /**
   * Returns true if this screen handled back press and should not be popped, false otherwise
   */
  fun handleBackPress(): Boolean = false
  
  /**
   * Called when screen is released
   */
  fun onRelease() = Unit
  
  /**
   * Called whenever the screen is brought to front to user
   */
  fun onAppearedOnScreen() = Unit
  
  /**
   * Called whenever the screen is brought to front to user and screen animation is completed
   */
  fun onAppearedOnScreenAfterAnimation() = Unit
  
  /**
   * Called whenever the screen is disappeared
   */
  fun onDisappearedFromScreen() = Unit
  
  /**
   * Called when orientation became portrait
   */
  fun onOrientationBecamePortrait() = Unit
  
  /**
   * Called when orientation became landscape
   */
  fun onOrientationBecameLandscape() = Unit
}