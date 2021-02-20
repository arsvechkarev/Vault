package com.arsvechkarev.vault.core.navigation

import com.arsvechkarev.vault.core.model.Service

/**
 * Host activity with necessary methods to be accessed from fragment
 */
interface Navigator {
  
  fun goToPasswordsListScreen()
  
  fun goToNewServiceScreen()
  
  fun goToSavedServiceInfoScreen(service: Service)
  
  fun popCurrentScreen()
}