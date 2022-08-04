package com.arsvechkarev.vault.core.di.modules

import android.app.Application
import buisnesslogic.GsonJsonConverter
import buisnesslogic.JsonConverter
import com.arsvechkarev.vault.core.DefaultDispatchersFacade
import com.arsvechkarev.vault.core.DispatchersFacade

interface CoreModule {
  val application: Application
  val dispatchersFacade: DispatchersFacade
  val jsonConverter: JsonConverter
}

class CoreModuleImpl(override val application: Application) : CoreModule {
  override val dispatchersFacade = DefaultDispatchersFacade
  override val jsonConverter = GsonJsonConverter
}
