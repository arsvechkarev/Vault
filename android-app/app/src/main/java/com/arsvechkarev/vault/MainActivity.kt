package com.arsvechkarev.vault

import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import com.arsvechkarev.vault.core.BaseActivity
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.di.AppComponent
import com.arsvechkarev.vault.core.di.AppComponentProvider
import com.arsvechkarev.vault.core.di.CoreComponentHolder
import com.arsvechkarev.vault.views.RootView
import viewdsl.Size.Companion.MatchParent
import viewdsl.ViewDslConfiguration
import viewdsl.id
import viewdsl.size
import viewdsl.withViewBuilder

class MainActivity : BaseActivity(), AppComponentProvider {
  
  private val mainActivityLayout
    get() = withViewBuilder {
      RootView(context).apply {
        id(rootViewId)
        size(MatchParent, MatchParent)
        fitsSystemWindows = true
      }
    }
  
  private var _appComponent: AppComponent? = null
  
  override val appComponent: AppComponent
    get() = checkNotNull(_appComponent) {
      "App component cannot be accessed before onCreate() or after onDestroy()"
    }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ViewDslConfiguration.initializeResources(resources)
    window.decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE
        or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    setContentView(mainActivityLayout)
    _appComponent = AppComponent.create(CoreComponentHolder.coreComponent, rootViewId, this)
    figureOutScreenToGo(savedInstanceState)
  }
  
  private fun figureOutScreenToGo(savedInstanceState: Bundle?) {
    if (savedInstanceState != null) {
      // Activity is recreated, navigator handles this case automatically
      return
    }
    if (appComponent.authChecker.isUserLoggedIn()) {
      appComponent.router.goForward(Screens.LoginScreen)
    } else {
      appComponent.router.goForward(Screens.InitialScreen)
    }
  }
  
  override fun onResume() {
    super.onResume()
    appComponent.navigationController.getNavigatorHolder().setNavigator(appComponent.navigator)
  }
  
  override fun onPause() {
    super.onPause()
    appComponent.navigationController.getNavigatorHolder().removeNavigator()
  }
  
  override fun onBackPressed() {
    if (!appComponent.navigator.handleGoBack()) {
      super.onBackPressed()
    }
  }
  
  override fun onDestroy() {
    super.onDestroy()
    _appComponent = null
  }
  
  private companion object {
    
    val rootViewId = View.generateViewId()
  }
}
