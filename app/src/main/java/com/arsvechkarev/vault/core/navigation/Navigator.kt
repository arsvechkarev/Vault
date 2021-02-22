package com.arsvechkarev.vault.core.navigation

import com.arsvechkarev.vault.core.model.Service

/**
 * Host activity with necessary methods to be accessed from fragment
 */
interface Navigator {
  
  fun goToCreatingMasterPasswordScreen()
  
  fun goToServicesListScreen()
  
  fun goToNewServiceScreen()
  
  fun goToInfoScreen(service: Service)
  
  fun popCurrentScreen()
}