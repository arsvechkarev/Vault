package com.arsvechkarev.vault

import android.app.Application
import com.arsvechkarev.vault.core.DefaultDispatchersFacade
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder
import com.arsvechkarev.vault.features.common.di.RealExtraDependenciesFactory
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.Styles
import timber.log.Timber
import viewdsl.ViewDslConfiguration

class VaultApplication : Application() {
  
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    ViewDslConfiguration.initializeWithAppContext(this)
    ViewDslConfiguration.setCoreStyles(Styles)
    Fonts.init(applicationContext)
    val extraDependenciesFactory = RealExtraDependenciesFactory(this, DefaultDispatchersFacade)
    CoreComponentHolder.initialize(this, extraDependenciesFactory)
  }
}
