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
import com.arsvechkarev.vault.viewdsl.Densities
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.id
import com.arsvechkarev.vault.viewdsl.size
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import com.arsvechkarev.vault.views.RootView

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
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Densities.init(resources)
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
      appComponent.router.goForward(Screens.StartScreen)
    } else {
      appComponent.router.goForward(Screens.InitialScreen)
    }
  }
  
  override fun onResume() {
    super.onResume()
    appComponent.cicerone.getNavigatorHolder().setNavigator(appComponent.navigator)
  }
  
  override fun onPause() {
    super.onPause()
    appComponent.cicerone.getNavigatorHolder().removeNavigator()
  }
  
  override fun onBackPressed() {
    if (!appComponent.navigator.handleGoBack()) {
      super.onBackPressed()
    }
  }
  
  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    appComponent.navigator.onSaveInstanceState(outState)
  }
  
  override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    appComponent.navigator.onRestoreInstanceState(savedInstanceState)
  }
  
  override fun onDestroy() {
    super.onDestroy()
    appComponent.navigator.releaseScreens()
    _appComponent = null
  }
  
  override val appComponent: AppComponent
    get() = checkNotNull(_appComponent) {
      "App component cannot be accessed before onCreate() or after onDestroy()"
    }
  
  private companion object {
    
    val rootViewId = View.generateViewId()
  }
}
