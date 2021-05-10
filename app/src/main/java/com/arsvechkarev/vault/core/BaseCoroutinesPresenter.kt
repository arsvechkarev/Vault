package com.arsvechkarev.vault.core

import androidx.annotation.CallSuper
import com.arsvechkarev.vault.core.communicators.Communicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpPresenter
import moxy.MvpView

abstract class BaseCoroutinesPresenter<V : MvpView>(
  protected val dispatchers: Dispatchers
) : MvpPresenter<V>() {
  
  protected val scope = CoroutineScope(SupervisorJob() + dispatchers.Main)
  
  private val channelsToListenersMap = HashMap<Communicator<out Any>, ArrayList<(Any) -> Unit>>()
  
  protected fun <E : Any> subscribeToCommunicator(communicator: Communicator<E>, listener: (E) -> Unit) {
    val list = (channelsToListenersMap[communicator] ?: ArrayList())
    @Suppress("UNCHECKED_CAST")
    list.add(listener as ((Any) -> Unit))
    channelsToListenersMap[communicator] = list
    communicator.subscribe(listener)
  }
  
  @CallSuper
  override fun onDestroy() {
    super.onDestroy()
    scope.cancel()
    channelsToListenersMap.forEach { entry ->
      val listeners = entry.value
      val channel = entry.key
      listeners.forEach { listener -> channel.unsubscribe(listener) }
    }
    channelsToListenersMap.clear()
  }
  
  protected fun coroutine(block: suspend () -> Unit) {
    scope.launch { block() }
  }
  
  protected suspend fun <T> onIoThread(block: suspend () -> T): T {
    return withContext(dispatchers.IO) { block() }
  }
  
  protected suspend fun <T> onBackgroundThread(block: suspend () -> T): T {
    return withContext(dispatchers.Default) { block() }
  }
}