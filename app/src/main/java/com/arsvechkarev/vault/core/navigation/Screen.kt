package com.arsvechkarev.vault.core.navigation

/**
 * Screen is interface for an application screen
 */
interface Screen {
  
  /**
   * Called when screen is initialized the first time
   */
  fun onInit(arguments: Map<String, Any>) = Unit
  
  /**
   * Returns true if allow popping this screen, false otherwise
   */
  fun allowBackPress(): Boolean = true
  
  /**
   * Called when screen is released
   */
  fun onRelease() = Unit
  
  /**
   * Called whenever the screen is brought to front to user
   */
  fun onAppearedOnScreen(arguments: Map<String, Any>) = Unit
  
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