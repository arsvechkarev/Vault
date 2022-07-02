package com.arsvechkarev.vault.core.di.modules

import android.app.Application
import com.arsvechkarev.vault.core.DefaultDispatchersFacade
import com.arsvechkarev.vault.core.DispatchersFacade

interface CoreModule {
  val application: Application
  val dispatchersFacade: DispatchersFacade
}

class CoreModuleImpl(override val application: Application) : CoreModule {
  override val dispatchersFacade = DefaultDispatchersFacade
}
