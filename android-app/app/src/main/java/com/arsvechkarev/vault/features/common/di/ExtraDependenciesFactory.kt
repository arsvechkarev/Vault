package com.arsvechkarev.vault.features.common.di

import android.app.Application
import com.arsvechkarev.vault.core.DispatchersFacade

interface ExtraDependenciesFactory {
  
  fun getExtraDependencies(): ExtraDependencies
}

class RealExtraDependenciesFactory(
  private val application: Application,
  private val dispatchersFacade: DispatchersFacade
) : ExtraDependenciesFactory {
  
  override fun getExtraDependencies(): ExtraDependencies {
    return RealExtraDependencies(application, dispatchersFacade)
  }
}
