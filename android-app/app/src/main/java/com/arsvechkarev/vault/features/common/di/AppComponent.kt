package com.arsvechkarev.vault.features.common.di

import androidx.fragment.app.FragmentActivity
import com.arsvechkarev.vault.features.common.di.modules.NavigationModule
import com.arsvechkarev.vault.features.common.di.modules.NavigationModuleImpl

class AppComponent(
  private val coreComponent: CoreComponent,
  private val navigationModule: NavigationModule,
) : CoreComponent by coreComponent,
  NavigationModule by navigationModule {
  
  companion object {
    
    fun create(
      coreComponent: CoreComponent,
      navigationRootViewId: Int,
      activity: FragmentActivity
    ): AppComponent {
      val navigationModule = NavigationModuleImpl(activity, navigationRootViewId)
      return AppComponent(coreComponent, navigationModule)
    }
  }
}
