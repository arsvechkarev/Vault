package com.arsvechkarev.vault.features.common.di.modules

import androidx.fragment.app.FragmentActivity
import com.arsvechkarev.vault.VaultApplication
import com.arsvechkarev.vault.features.common.Router
import navigation.FragmentNavigatorImpl
import navigation.NavigationController
import navigation.Navigator

interface NavigationModule {
  val router: Router
  val navigationController: NavigationController
  // TODO (27.11.2022): Extract navigator to separate di module
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
