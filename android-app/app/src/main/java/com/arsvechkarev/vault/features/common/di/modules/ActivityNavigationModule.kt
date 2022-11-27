package com.arsvechkarev.vault.features.common.di.modules

import androidx.fragment.app.FragmentActivity
import navigation.FragmentNavigatorImpl
import navigation.Navigator

interface ActivityNavigationModule {
  val navigator: Navigator
}

class ActivityNavigationModuleImpl(
  activity: FragmentActivity,
  navigationRootViewId: Int,
) : ActivityNavigationModule {
  
  override val navigator = FragmentNavigatorImpl(activity, navigationRootViewId)
}
