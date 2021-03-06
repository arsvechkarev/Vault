package com.arsvechkarev.vault.core.navigation

/**
 * Handles interactions with the Screen
 */
interface ScreenHandler {
  
  val animationDelay: Long
  
  fun setupArguments(arguments: Map<String, Any>)
  
  fun buildViewIfNeeded()
  
  fun performInit()
  
  fun performShow(type: AnimationType)
  
  fun performHide(type: AnimationType)
  
  fun performRelease()
  
  fun allowBackPress(): Boolean
  
  fun onOrientationBecamePortrait()
  
  fun onOrientationBecameLandscape()
}