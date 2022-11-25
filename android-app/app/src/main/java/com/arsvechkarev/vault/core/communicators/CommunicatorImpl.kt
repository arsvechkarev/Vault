package com.arsvechkarev.vault.core.communicators

import kotlinx.coroutines.flow.MutableSharedFlow

class CommunicatorImpl<Input, Output>(
  override val input: MutableSharedFlow<Input> = MutableSharedFlow(replay = 1),
  override val output: MutableSharedFlow<Output> = MutableSharedFlow(),
) : Communicator<Input, Output>
