package com.arsvechkarev.vault.features.common.domain

import domain.Password
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

sealed interface CreatingPasswordMode {
  object CreateNew : CreatingPasswordMode
  class EditExisting(val password: Password) : CreatingPasswordMode
}

interface CreatingPasswordSetupObserver {
  
  val passwordModeFlow: Flow<CreatingPasswordMode>
  
  fun setup(mode: CreatingPasswordMode)
}

class CreatingPasswordSetupObserverImpl : CreatingPasswordSetupObserver {
  
  private val _passwordModeFlow = MutableSharedFlow<CreatingPasswordMode>(
    replay = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
  )
  
  override val passwordModeFlow: Flow<CreatingPasswordMode>
    get() = _passwordModeFlow
  
  override fun setup(mode: CreatingPasswordMode) {
    _passwordModeFlow.tryEmit(mode)
  }
}
