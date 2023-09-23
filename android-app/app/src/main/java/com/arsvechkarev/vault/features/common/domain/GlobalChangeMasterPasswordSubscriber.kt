package com.arsvechkarev.vault.features.common.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface GlobalChangeMasterPasswordSubscriber {
  val masterPasswordChanges: Flow<Unit>
}

interface GlobalChangeMasterPasswordPublisher {
  fun publishMasterPasswordChanged()
}

class GlobalChangeMasterPasswordImpl : GlobalChangeMasterPasswordSubscriber,
  GlobalChangeMasterPasswordPublisher {
  
  private val _masterPasswordChanges = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
  
  override val masterPasswordChanges = _masterPasswordChanges
  
  override fun publishMasterPasswordChanged() {
    _masterPasswordChanges.tryEmit(Unit)
  }
}
