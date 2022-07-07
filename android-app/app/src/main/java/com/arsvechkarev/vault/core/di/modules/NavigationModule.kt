package com.arsvechkarev.vault.core.di.modules

import android.widget.FrameLayout
import androidx.annotation.IdRes
import com.arsvechkarev.vault.VaultApplication
import com.arsvechkarev.vault.core.Router
import com.github.terrakok.cicerone.Cicerone
import moxy.MvpAppCompatActivity
import navigation.BaseScreen
import navigation.ExtendedNavigator
import navigation.ExtendedNavigatorImpl
import navigation.MvpViewScreenHandler
import navigation.OfClassNameFactory
import navigation.ScreenHandler
import navigation.ScreenHandlerFactory
import navigation.ViewNavigationHost

interface NavigationModule {
  val router: Router
  val cicerone: Cicerone<Router>
  val navigator: ExtendedNavigator
}

class NavigationModuleImpl(
  activity: MvpAppCompatActivity,
  @IdRes navigationRootViewId: Int,
) : NavigationModule {
  
  override val router = Router(VaultApplication.AppMainCoroutineScope)
  
  override val cicerone = Cicerone.create(router)
  
  override val navigator = createNavigator(activity, navigationRootViewId)
  
  private fun createNavigator(activity: MvpAppCompatActivity, rootViewId: Int): ExtendedNavigator {
    val rootView = activity.findViewById<FrameLayout>(rootViewId)
    val screenHandlerViewProvider = { handler: ScreenHandler -> (handler as MvpViewScreenHandler).view }
    val navHost = ViewNavigationHost(rootView, screenHandlerViewProvider)
    val screenHandlerFactory = ScreenHandlerFactory { screenKey, screen ->
      MvpViewScreenHandler(screen as BaseScreen, screenKey.toString(),
        activity.mvpDelegate, activity)
    }
    return ExtendedNavigatorImpl(navHost, OfClassNameFactory, screenHandlerFactory)
  }
}
