package com.arsvechkarev.vault.features.common.domain

/**
 * Checks user authentication
 */
interface AuthChecker {
  
  /**
   * Set that user is saved master password and now can enter the app using in
   */
  fun setUserIsLoggedIn()
  
  /**
   * Check whether user is logged in or not
   */
  fun isUserLoggedIn(): Boolean
}
