package com.arsvechkarev.vault.features.main_list.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.domain.ReloadImagesObserver
import com.arsvechkarev.vault.features.main_list.MainListCommand
import com.arsvechkarev.vault.features.main_list.MainListEvent
import com.arsvechkarev.vault.features.main_list.MainListEvent.RequestReloadImages
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class ListenReloadPasswordImagesActor(
  private val reloadImagesObserver: ReloadImagesObserver,
) : Actor<MainListCommand, MainListEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<MainListCommand>): Flow<MainListEvent> {
    return reloadImagesObserver.reloadsFlow
        .mapLatest { RequestReloadImages }
  }
}