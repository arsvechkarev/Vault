package com.arsvechkarev.vault

import android.app.Application
import com.arsvechkarev.vault.core.DefaultDispatchersFacade
import com.arsvechkarev.vault.core.VaultNavigationAnimations
import com.arsvechkarev.vault.core.di.CoreComponentHolder
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.Styles
import kotlinx.coroutines.CoroutineScope
import navigation.NavigationConfig
import timber.log.Timber
import viewdsl.ViewDslConfiguration

class VaultApplication : Application() {
  
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    ViewDslConfiguration.initializeWithAppContext(this)
    ViewDslConfiguration.setDefaultStyles(Styles)
    NavigationConfig.setAnimations(VaultNavigationAnimations)
    Fonts.init(applicationContext)
    CoreComponentHolder.createCoreComponent(this)
  }
  
  companion object {
    
    val AppMainCoroutineScope = CoroutineScope(DefaultDispatchersFacade.Main)
  }
}