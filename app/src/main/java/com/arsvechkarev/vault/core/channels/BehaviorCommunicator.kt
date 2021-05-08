package com.arsvechkarev.vault.core.channels

/**
 * Communicator that behaves like BehaviorSubject in RxJava: when new subscriber is added, it receives
 * latest event if there was one
 */
class BehaviorCommunicator<E> : Communicator<E> {
  
  private val subscribers = ArrayList<(E) -> Unit>()
  private var latestEvent: E? = null
  
  override fun send(event: E) {
    latestEvent = event
    subscribers.forEach { it.invoke(event) }
  }
  
  override fun subscribe(listener: (E) -> Unit) {
    subscribers.add(listener)
    latestEvent?.let(listener::invoke)
  }
  
  override fun unsubscribe(listener: (E) -> Unit) {
    subscribers.remove(listener)
  }
}