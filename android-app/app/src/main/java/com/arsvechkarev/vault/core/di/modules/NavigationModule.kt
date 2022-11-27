package com.arsvechkarev.vault.core.di.modules

import androidx.fragment.app.FragmentActivity
import com.arsvechkarev.vault.VaultApplication
import com.arsvechkarev.vault.features.common.Router
import navigation.FragmentNavigatorImpl
import navigation.NavigationController
import navigation.Navigator

interface NavigationModule {
  val router: Router
  val navigationController: NavigationController
  val navigator: Navigator
}

class NavigationModuleImpl(
  activity: FragmentActivity,
  navigationRootViewId: Int,
) : NavigationModule {
  
  override val router = Router(VaultApplication.AppMainCoroutineScope)
  
  override val navigationController = NavigationController.create(router)
  
  override val navigator = createNavigator(activity, navigationRootViewId)
  
  private fun createNavigator(activity: FragmentActivity, rootViewId: Int): Navigator {
    return FragmentNavigatorImpl(activity, rootViewId)
  }
}
