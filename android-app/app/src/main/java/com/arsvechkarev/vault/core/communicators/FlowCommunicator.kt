package com.arsvechkarev.vault.core.communicators

import kotlinx.coroutines.flow.Flow

/**
 * Communicator for sending and receiving events using flow
 *
 * @param E types of events
 */
interface FlowCommunicator<E> {
  
  /**
   * Flow of events
   */
  val events: Flow<E>
  
  /**
   * Sends [event] to all current subscribers
   */
  suspend fun send(event: E)
}