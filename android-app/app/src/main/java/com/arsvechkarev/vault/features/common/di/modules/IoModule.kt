package com.arsvechkarev.vault.features.common.di.modules

import com.arsvechkarev.vault.features.common.data.ExternalFileReader
import com.arsvechkarev.vault.features.common.data.FilenameFromUriRetriever
import com.arsvechkarev.vault.features.common.data.FilenameFromUriRetrieverImpl
import com.arsvechkarev.vault.features.common.data.PasswordsFileExporter

interface IoModule {
  val filenameFromUriRetriever: FilenameFromUriRetriever
  val externalFileReader: ExternalFileReader
  val passwordsFileExporter: PasswordsFileExporter
}

class IoModuleImpl(
  coreModule: CoreModule,
  override val externalFileReader: ExternalFileReader,
  override val passwordsFileExporter: PasswordsFileExporter
) : IoModule {
  
  
  override val filenameFromUriRetriever = FilenameFromUriRetrieverImpl(coreModule.application)
  
  companion object {
    
    const val FILENAME = "passwords.kdbx"
  }
}
