package com.arsvechkarev.vault.core.channels

/**
 * Communicator that behaves like PublishSubject in RxJava: notifies all current listeners about event
 */
class PublishCommunicator<E> : Communicator<E> {
  
  private val subscribers = ArrayList<(E) -> Unit>()
  
  override fun send(event: E) {
    subscribers.forEach { it.invoke(event) }
  }
  
  override fun subscribe(listener: (E) -> Unit) {
    subscribers.add(listener)
  }
  
  override fun unsubscribe(listener: (E) -> Unit) {
    subscribers.remove(listener)
  }
}