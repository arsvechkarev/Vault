package com.arsvechkarev.vault.core.navigation

import com.arsvechkarev.vault.core.model.Service

interface AppNavigator {
  
  fun goToCreatingMasterPasswordScreen()
  
  fun goToServicesListScreen()
  
  fun goToNewServiceScreen()
  
  fun goToInfoScreen(service: Service)
  
  fun popCurrentScreen()
}