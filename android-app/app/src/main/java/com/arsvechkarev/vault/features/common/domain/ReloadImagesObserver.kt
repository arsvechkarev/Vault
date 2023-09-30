package com.arsvechkarev.vault.features.common.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface ReloadImagesObserver {
  
  val reloadsFlow: Flow<Unit>
  
  fun requestReload()
}

class ReloadImagesObserverImpl : ReloadImagesObserver {
  
  private val _reloadsFlow = MutableSharedFlow<Unit>(replay = 1)
  
  override val reloadsFlow: Flow<Unit> get() = _reloadsFlow
  
  override fun requestReload() {
    _reloadsFlow.tryEmit(Unit)
  }
}
