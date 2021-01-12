package com.arsvechkarev.vault.core.navigation

/**
 * Host activity with necessary methods to be accessed from fragment
 */
interface Navigator {
  
  fun popCurrentScreen(notifyBackPress: Boolean = true)
}