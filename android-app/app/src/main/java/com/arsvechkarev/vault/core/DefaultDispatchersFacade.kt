package com.arsvechkarev.vault.core

import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers as CoroutinesDispatchers

object DefaultDispatchersFacade : DispatchersFacade {
  
  override val IO = CoroutinesDispatchers.IO
  override val SingleThread = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
  override val Default = CoroutinesDispatchers.Default
  override val Main = CoroutinesDispatchers.Main.immediate
}
