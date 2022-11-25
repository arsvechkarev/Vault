package com.arsvechkarev.vault.core

import kotlinx.coroutines.Dispatchers as CoroutinesDispatchers

object DefaultDispatchersFacade : DispatchersFacade {
  
  override val IO = CoroutinesDispatchers.IO
  override val Default = CoroutinesDispatchers.Default
  override val Main = CoroutinesDispatchers.Main.immediate
}