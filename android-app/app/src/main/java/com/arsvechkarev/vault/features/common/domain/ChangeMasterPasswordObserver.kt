package com.arsvechkarev.vault.features.common.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface ChangeMasterPasswordObserver {
  
  val masterPasswordChanges: Flow<Unit>
  
  fun sendMasterPasswordChangedEvent()
}

class ChangeMasterPasswordObserverImpl : ChangeMasterPasswordObserver {
  
  private val _masterPasswordChanges = MutableSharedFlow<Unit>(replay = 1)
  
  override val masterPasswordChanges = _masterPasswordChanges
  
  override fun sendMasterPasswordChangedEvent() {
    _masterPasswordChanges.tryEmit(Unit)
  }
}
