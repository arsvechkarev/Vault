package com.arsvechkarev.vault.core.di.modules

import android.widget.FrameLayout
import androidx.annotation.IdRes
import com.arsvechkarev.vault.VaultApplication
import com.arsvechkarev.vault.core.Router
import moxy.MvpAppCompatActivity
import navigation.BaseScreen
import navigation.MvpViewScreenHandler
import navigation.NavigationController
import navigation.Navigator
import navigation.NavigatorImpl
import navigation.OfClassNameFactory
import navigation.ScreenHandler
import navigation.ScreenHandlerFactory
import navigation.ViewNavigationHost

interface NavigationModule {
  val router: Router
  val navigationController: NavigationController
  val navigator: Navigator
}

class NavigationModuleImpl(
  activity: MvpAppCompatActivity,
  @IdRes navigationRootViewId: Int,
) : NavigationModule {
  
  override val router = Router(VaultApplication.AppMainCoroutineScope)
  
  override val navigationController = NavigationController.create(router)
  
  override val navigator = createNavigator(activity, navigationRootViewId)
  
  private fun createNavigator(activity: MvpAppCompatActivity, rootViewId: Int): Navigator {
    val rootView = activity.findViewById<FrameLayout>(rootViewId)
    val screenHandlerViewProvider = { handler: ScreenHandler ->
      (handler as MvpViewScreenHandler).view
    }
    val navHost = ViewNavigationHost(rootView, screenHandlerViewProvider)
    val screenHandlerFactory = ScreenHandlerFactory { screenKey, screen ->
      MvpViewScreenHandler(screen as BaseScreen, screenKey.toString(),
        activity.mvpDelegate, activity)
    }
    return NavigatorImpl(navHost, OfClassNameFactory, screenHandlerFactory)
  }
}
