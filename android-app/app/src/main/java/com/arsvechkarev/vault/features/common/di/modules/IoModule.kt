package com.arsvechkarev.vault.features.common.di.modules

import com.arsvechkarev.vault.features.common.data.files.ExternalFileReader
import com.arsvechkarev.vault.features.common.data.files.FilenameFromUriRetriever
import com.arsvechkarev.vault.features.common.data.files.FilenameFromUriRetrieverImpl
import com.arsvechkarev.vault.features.common.data.files.PasswordsFileExporter
import com.arsvechkarev.vault.features.common.data.network.AndroidNetworkConnectivityProvider
import com.arsvechkarev.vault.features.common.data.network.NetworkConnectivityProvider
import com.arsvechkarev.vault.features.common.data.network.NetworkUrlLoader

interface IoModule {
  val filenameFromUriRetriever: FilenameFromUriRetriever
  val externalFileReader: ExternalFileReader
  val passwordsFileExporter: PasswordsFileExporter
  val networkUrlLoader: NetworkUrlLoader
  val networkConnectivityProvider: NetworkConnectivityProvider
}

class IoModuleImpl(
  coreModule: CoreModule,
  override val externalFileReader: ExternalFileReader,
  override val passwordsFileExporter: PasswordsFileExporter
) : IoModule {
  
  override val filenameFromUriRetriever = FilenameFromUriRetrieverImpl(coreModule.application)
  
  override val networkUrlLoader = NetworkUrlLoader(coreModule.okHttpClient, coreModule.dispatchers)
  
  override val networkConnectivityProvider =
      AndroidNetworkConnectivityProvider(coreModule.application)
}
