package com.arsvechkarev.vault.features.settings.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.images.ImagesCache
import com.arsvechkarev.vault.features.common.domain.ImagesNamesLoader
import com.arsvechkarev.vault.features.settings.SettingsCommand
import com.arsvechkarev.vault.features.settings.SettingsCommand.ClearImagesCache
import com.arsvechkarev.vault.features.settings.SettingsEvent
import com.arsvechkarev.vault.features.settings.SettingsEvent.ImagesCacheCleared
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class ClearImagesCacheActor(
  private val imagesCache: ImagesCache,
  private val imagesNamesLoader: ImagesNamesLoader,
) : Actor<SettingsCommand, SettingsEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<SettingsCommand>): Flow<SettingsEvent> {
    return commands.filterIsInstance<ClearImagesCache>()
        .mapLatest {
          imagesCache.clearAll()
          imagesNamesLoader.clear()
          ImagesCacheCleared
        }
  }
}