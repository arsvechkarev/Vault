package com.arsvechkarev.vault.core

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpPresenter
import moxy.MvpView

abstract class BasePresenter<V : MvpView>(
  protected val dispatchers: DispatchersFacade,
) : MvpPresenter<V>(), CoroutineScope {
  
  override val coroutineContext = dispatchers.Main + SupervisorJob()
  
  protected suspend fun <T> onIoThread(block: suspend () -> T): T {
    return withContext(dispatchers.IO) { block() }
  }
  
  protected suspend fun <T> onBackgroundThread(block: suspend () -> T): T {
    return withContext(dispatchers.Default) { block() }
  }
  
  protected fun <T> Flow<T>.collectInPresenterScope(block: suspend (T) -> Unit) {
    launch { collect { block(it) } }
  }
  
  @CallSuper
  override fun onDestroy() {
    super.onDestroy()
    cancel()
  }
}