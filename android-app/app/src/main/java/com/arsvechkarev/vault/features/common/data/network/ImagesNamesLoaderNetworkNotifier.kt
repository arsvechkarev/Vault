package com.arsvechkarev.vault.features.common.data.network

import com.arsvechkarev.vault.features.common.domain.ImagesNamesLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ImagesNamesLoaderNetworkNotifier(
  private val imagesNamesLoader: ImagesNamesLoader,
  private val networkConnectivityProvider: NetworkConnectivityProvider,
) {
  
  fun notifyAboutNetworkAvailability(scope: CoroutineScope) {
    networkConnectivityProvider.networkConnectivityFlow()
        .onEach { isAvailable -> if (isAvailable) imagesNamesLoader.loadFromNetwork() }
        .launchIn(scope)
  }
}
