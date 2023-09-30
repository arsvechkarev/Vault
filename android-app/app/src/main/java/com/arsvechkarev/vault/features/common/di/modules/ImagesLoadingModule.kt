package com.arsvechkarev.vault.features.common.di.modules

import buisnesslogic.IMAGES_CACHE_DIRECTORY
import com.arsvechkarev.vault.features.common.data.images.DiskImagesCache
import com.arsvechkarev.vault.features.common.data.images.ImagesCache
import com.arsvechkarev.vault.features.common.data.images.MemoryImagesCache
import com.arsvechkarev.vault.features.common.data.network.ImagesNamesLoaderNetworkNotifier
import com.arsvechkarev.vault.features.common.domain.ImagesNamesLoader

interface ImagesLoadingModule {
  val imagesNamesLoader: ImagesNamesLoader
  val imagesCache: ImagesCache
  val imagesNamesLoaderNetworkNotifier: ImagesNamesLoaderNetworkNotifier
}

class ImagesLoadingModuleImpl(
  coreModule: CoreModule,
  ioModule: IoModule,
  preferencesModule: PreferencesModule
) : ImagesLoadingModule {
  
  override val imagesNamesLoader = ImagesNamesLoader(
    ioModule.networkUrlLoader,
    preferencesModule.imagesNamesPreferences,
    coreModule.timestampProvider
  )
  
  override val imagesCache: ImagesCache =
      MemoryImagesCache(
        DiskImagesCache(
          coreModule.application,
          IMAGES_CACHE_DIRECTORY,
          coreModule.dispatchers
        )
      )
  
  override val imagesNamesLoaderNetworkNotifier =
      ImagesNamesLoaderNetworkNotifier(imagesNamesLoader, ioModule.networkConnectivityProvider)
}
