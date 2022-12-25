package com.arsvechkarev.vault.features.common.di.modules

import android.app.Application
import com.arsvechkarev.vault.core.DefaultDispatchersFacade
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.features.common.domain.Clipboard
import com.arsvechkarev.vault.features.common.domain.ClipboardImpl
import com.google.gson.Gson

interface CoreModule {
  val application: Application
  val dispatchersFacade: DispatchersFacade
  val clipboard: Clipboard
  val gson: Gson
}

class CoreModuleImpl(override val application: Application) : CoreModule {
  override val dispatchersFacade = DefaultDispatchersFacade
  override val clipboard = ClipboardImpl(application)
  override val gson = Gson()
}
