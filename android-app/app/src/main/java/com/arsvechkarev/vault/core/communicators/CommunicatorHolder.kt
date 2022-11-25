package com.arsvechkarev.vault.core.communicators

class CommunicatorHolder<Input, Output> {
  
  private var _communicator: Communicator<Input, Output>? = null
  
  val communicator: Communicator<Input, Output>
    get() = requireNotNull(_communicator) { "'startNewCommunication()' was not called" }
  
  fun startNewCommunication() {
    _communicator = CommunicatorImpl()
  }
  
  fun finishCommunication() {
    _communicator = null
  }
}