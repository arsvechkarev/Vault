package com.arsvechkarev.vault.core.communicators

import kotlinx.coroutines.flow.MutableSharedFlow

class FlowCommunicatorImpl<E>(
  private val flow: MutableSharedFlow<E> = MutableSharedFlow<E>()
) : FlowCommunicator<E> {
  
  override val events = flow
  
  override suspend fun send(event: E) {
    flow.emit(event)
  }
}