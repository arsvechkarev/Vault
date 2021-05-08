package com.arsvechkarev.vault.core

import androidx.annotation.CallSuper
import com.arsvechkarev.vault.core.channels.Communicator
import moxy.MvpView

abstract class BasePresenterWithChannels<V : MvpView>(threader: Threader) :
  BasePresenter<V>(threader) {
  
  private val channelsToListenersMap = HashMap<Communicator<out Any>, ArrayList<(Any) -> Unit>>()
  
  protected fun <E : Any> subscribeToChannel(communicator: Communicator<E>, listener: (E) -> Unit) {
    val list = (channelsToListenersMap[communicator] ?: ArrayList())
    @Suppress("UNCHECKED_CAST")
    list.add(listener as ((Any) -> Unit))
    channelsToListenersMap[communicator] = list
    communicator.subscribe(listener)
  }
  
  @CallSuper
  override fun onDestroy() {
    super.onDestroy()
    channelsToListenersMap.forEach { entry ->
      val listeners = entry.value
      val channel = entry.key
      listeners.forEach { listener -> channel.unsubscribe(listener) }
    }
    channelsToListenersMap.clear()
  }
}