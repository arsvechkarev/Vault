package com.arsvechkarev.vault.features.main

import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import com.arsvechkarev.vault.core.BaseActivity
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.UserAuthSaver
import com.arsvechkarev.vault.core.di.CoreComponent
import com.arsvechkarev.vault.cryptography.MasterPasswordHolder
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewdsl.Densities
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.id
import com.arsvechkarev.vault.viewdsl.size
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import com.arsvechkarev.vault.views.RootView
import com.github.terrakok.cicerone.Cicerone
import navigation.ExtendedNavigator
import navigation.Router
import javax.inject.Inject

class MainActivity : BaseActivity() {
  
  private val mainActivityLayout
    get() = withViewBuilder {
      RootView(context).apply {
        id(rootViewId)
        size(MatchParent, MatchParent)
        fitsSystemWindows = true
      }
    }
  
  @Inject
  lateinit var navigator: ExtendedNavigator
  
  @Inject
  lateinit var cicerone: Cicerone<Router>
  
  @Inject
  lateinit var router: Router
  
  @Inject
  lateinit var userAuthSaver: UserAuthSaver
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Densities.init(resources)
    Colors.init(this)
    window.decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE
        or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    setContentView(mainActivityLayout)
    CoreComponent.instance.getMainComponentBuilder()
        .activity(this)
        .rootViewId(rootViewId)
        .build()
        .inject(this)
    figureOutScreenToGo(savedInstanceState)
  }
  
  private fun figureOutScreenToGo(savedInstanceState: Bundle?) {
    if (savedInstanceState != null) {
      // Activity is recreated, navigator handles this case automatically
      return
    }
    if (userAuthSaver.isUserAuthorized()) {
      MasterPasswordHolder.setMasterPassword("qwerty??123")
      router.goForward(Screens.ServicesListScreen)
      //      router.goForward(Screens.StartScreen)
    } else {
      //      router.goForward(Screens.InitialScreen)
    }
  }
  
  override fun onResume() {
    super.onResume()
    cicerone.getNavigatorHolder().setNavigator(navigator)
  }
  
  override fun onPause() {
    super.onPause()
    cicerone.getNavigatorHolder().removeNavigator()
  }
  
  override fun onBackPressed() {
    if (!navigator.handleGoBack()) {
      super.onBackPressed()
    }
  }
  
  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    navigator.onSaveInstanceState(outState)
  }
  
  override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    navigator.onRestoreInstanceState(savedInstanceState)
  }
  
  override fun onDestroy() {
    super.onDestroy()
    navigator.releaseScreens()
  }
  
  private companion object {
    
    val rootViewId = View.generateViewId()
  }
}