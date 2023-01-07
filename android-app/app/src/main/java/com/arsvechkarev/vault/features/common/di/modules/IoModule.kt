package com.arsvechkarev.vault.features.common.di.modules

import buisnesslogic.FileSaver
import com.arsvechkarev.vault.features.common.data.FileReader
import com.arsvechkarev.vault.features.common.data.FileReaderImpl
import com.arsvechkarev.vault.features.common.data.FileSaverImpl
import com.arsvechkarev.vault.features.common.data.FilenameFromUriRetriever
import com.arsvechkarev.vault.features.common.data.FilenameFromUriRetrieverImpl
import com.arsvechkarev.vault.features.common.data.PasswordsFileExporter

interface IoModule {
  val fileSaver: FileSaver
  val filenameFromUriRetriever: FilenameFromUriRetriever
  val fileReader: FileReader
  val passwordsFileExporter: PasswordsFileExporter
}

class IoModuleImpl(
  coreModule: CoreModule,
  override val passwordsFileExporter: PasswordsFileExporter
) : IoModule {
  
  override val fileSaver = FileSaverImpl(
    FILENAME,
    coreModule.application,
    coreModule.dispatchersFacade
  )
  
  override val filenameFromUriRetriever = FilenameFromUriRetrieverImpl(coreModule.application)
  
  override val fileReader = FileReaderImpl(coreModule.application)
  
  companion object {
    
    const val FILENAME = "passwords.vault"
  }
}
