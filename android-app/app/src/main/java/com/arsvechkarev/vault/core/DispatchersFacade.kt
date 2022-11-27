package com.arsvechkarev.vault.core

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Dispatchers for coroutines
 */
interface DispatchersFacade {
  
  val IO: CoroutineDispatcher
  val Default: CoroutineDispatcher
  val Main: CoroutineDispatcher
}
