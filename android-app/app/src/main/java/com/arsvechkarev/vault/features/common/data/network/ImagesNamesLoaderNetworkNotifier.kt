package com.arsvechkarev.vault.features.common.data.network

import com.arsvechkarev.vault.features.common.domain.images_names.ImagesNamesLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ImagesNamesLoaderNetworkNotifier(
  private val imagesNamesLoader: ImagesNamesLoader,
  private val networkAvailabilityProvider: NetworkAvailabilityProvider,
) {
  
  fun notifyAboutNetworkAvailability(scope: CoroutineScope) {
    networkAvailabilityProvider.availabilityFlow
        .onEach { isAvailable -> if (isAvailable) imagesNamesLoader.loadFromNetwork() }
        .launchIn(scope)
  }
}
