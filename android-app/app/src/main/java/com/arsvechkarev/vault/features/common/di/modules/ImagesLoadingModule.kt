package com.arsvechkarev.vault.features.common.di.modules

import com.arsvechkarev.vault.features.common.AppConstants.IMAGES_CACHE_DIRECTORY
import com.arsvechkarev.vault.features.common.data.images.DiskImagesCache
import com.arsvechkarev.vault.features.common.data.images.ImagesCache
import com.arsvechkarev.vault.features.common.data.network.ImagesNamesLoaderNetworkNotifier
import com.arsvechkarev.vault.features.common.domain.ImageRequestsRecorder
import com.arsvechkarev.vault.features.common.domain.ImagesLoadingEnabledInteractor
import com.arsvechkarev.vault.features.common.domain.ReloadImagesObserver
import com.arsvechkarev.vault.features.common.domain.ReloadImagesObserverImpl
import com.arsvechkarev.vault.features.common.domain.images_names.ImagesNamesLoader

interface ImagesLoadingModule {
  val imagesNamesLoader: ImagesNamesLoader
  val imagesCache: ImagesCache
  val imageRequestsRecorder: ImageRequestsRecorder
  val imagesNamesLoaderNetworkNotifier: ImagesNamesLoaderNetworkNotifier
  val reloadImagesObserver: ReloadImagesObserver
  val imagesLoadingEnabledInteractor: ImagesLoadingEnabledInteractor
}

class ImagesLoadingModuleImpl(
  coreModule: CoreModule,
  ioModule: IoModule,
  preferencesModule: PreferencesModule,
  override val imageRequestsRecorder: ImageRequestsRecorder
) : ImagesLoadingModule {
  
  override val imagesNamesLoader = ImagesNamesLoader(
    ioModule.networkUrlLoader,
    preferencesModule.imagesNamesPreferences,
    coreModule.timestampProvider
  )
  
  override val imagesCache = DiskImagesCache(
    coreModule.application,
    IMAGES_CACHE_DIRECTORY,
    coreModule.dispatchers
  )
  
  override val imagesNamesLoaderNetworkNotifier =
      ImagesNamesLoaderNetworkNotifier(imagesNamesLoader, ioModule.networkAvailabilityProvider)
  
  override val reloadImagesObserver = ReloadImagesObserverImpl()
  
  override val imagesLoadingEnabledInteractor = ImagesLoadingEnabledInteractor(
    imagesCache,
    imagesNamesLoader,
    reloadImagesObserver,
    preferencesModule.settingsPreferences
  )
}
