package com.arsvechkarev.vault.features.common.di.modules

import com.arsvechkarev.vault.features.common.data.files.ExternalFileReader
import com.arsvechkarev.vault.features.common.data.files.KeyFileSaver
import com.arsvechkarev.vault.features.common.data.files.PasswordsFileExporter
import com.arsvechkarev.vault.features.common.data.files.UriPersistedMaker
import com.arsvechkarev.vault.features.common.data.network.AndroidNetworkAvailabilityProvider
import com.arsvechkarev.vault.features.common.data.network.NetworkAvailabilityProvider
import com.arsvechkarev.vault.features.common.data.network.NetworkUrlLoader
import com.arsvechkarev.vault.features.common.data.network.OkHttpNetworkUrlLoader
import okhttp3.OkHttpClient

interface IoModule {
  val okHttpClient: OkHttpClient
  val externalFileReader: ExternalFileReader
  val passwordsFileExporter: PasswordsFileExporter
  val keyFileSaver: KeyFileSaver
  val networkUrlLoader: NetworkUrlLoader
  val networkAvailabilityProvider: NetworkAvailabilityProvider
  val uriPersistedMaker: UriPersistedMaker
}

class IoModuleImpl(
  coreModule: CoreModule,
  override val okHttpClient: OkHttpClient,
  override val externalFileReader: ExternalFileReader,
  override val passwordsFileExporter: PasswordsFileExporter,
  override val keyFileSaver: KeyFileSaver,
  override val uriPersistedMaker: UriPersistedMaker
) : IoModule {
  
  override val networkUrlLoader = OkHttpNetworkUrlLoader(okHttpClient, coreModule.dispatchers)
  
  override val networkAvailabilityProvider =
      AndroidNetworkAvailabilityProvider(coreModule.application)
}
