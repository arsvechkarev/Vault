package com.arsvechkarev.vault.core.di

import com.arsvechkarev.vault.core.di.modules.NavigationModule
import com.arsvechkarev.vault.core.di.modules.NavigationModuleImpl
import moxy.MvpAppCompatActivity

class AppComponent(
  private val coreComponent: CoreComponent,
  private val navigationModule: NavigationModule,
) : CoreComponent by coreComponent,
  NavigationModule by navigationModule {
  
  companion object {
    
    fun create(
      coreComponent: CoreComponent,
      navigationRootViewId: Int,
      activity: MvpAppCompatActivity
    ): AppComponent {
      val navigationModule = NavigationModuleImpl(activity, navigationRootViewId)
      return AppComponent(coreComponent, navigationModule)
    }
  }
}