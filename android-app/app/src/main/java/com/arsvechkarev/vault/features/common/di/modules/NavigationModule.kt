package com.arsvechkarev.vault.features.common.di.modules

import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.navigation.ActivityResultSubstitutor
import navigation.NavigationController

interface NavigationModule {
  val router: Router
  val navigationController: NavigationController
  val activityResultSubstitutor: ActivityResultSubstitutor
}

class NavigationModuleImpl(
  override val activityResultSubstitutor: ActivityResultSubstitutor
) : NavigationModule {
  
  override val router = Router()
  
  override val navigationController = NavigationController.create(router)
}
