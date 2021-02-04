package com.arsvechkarev.vault.core.navigation

import com.arsvechkarev.vault.core.model.ServiceInfo

/**
 * Host activity with necessary methods to be accessed from fragment
 */
interface Navigator {
  
  fun goToPasswordsListScreen()
  
  fun goToNewServiceScreen()
  
  fun goToSavedServiceInfoScreen(serviceInfo: ServiceInfo)
  
  fun showNewServiceSaved()
  
  fun popCurrentScreen()
}