package com.arsvechkarev.vault.features.common.di.modules

import com.arsvechkarev.vault.VaultApplication
import com.arsvechkarev.vault.features.common.Router
import navigation.NavigationController

interface NavigationModule {
  val router: Router
  val navigationController: NavigationController
}

class NavigationModuleImpl : NavigationModule {
  
  override val router = Router(VaultApplication.AppMainCoroutineScope)
  
  override val navigationController = NavigationController.create(router)
}
