package com.arsvechkarev.vault.test.core.stub

import com.arsvechkarev.vault.core.DispatchersFacade
import kotlinx.coroutines.Dispatchers

object TestDispatchersFacade : DispatchersFacade {
  override val IO = Dispatchers.Unconfined
  override val Default = Dispatchers.Unconfined
  override val Main = Dispatchers.Unconfined
}
