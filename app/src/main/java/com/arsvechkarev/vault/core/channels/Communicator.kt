package com.arsvechkarev.vault.core.channels

/**
 * Communicator for sending and receiving events. **NOT THREAD SAFE**
 *
 * @param E type of events
 */
interface Communicator<E> {
  
  /**
   * Sends [event] to all current subscribers
   */
  fun send(event: E)
  
  /**
   * Adds [listener] to the list of subscribers
   */
  fun subscribe(listener: (E) -> Unit)
  
  /**
   * Removes [listener] from the list of subscribers
   */
  fun unsubscribe(listener: (E) -> Unit)
}