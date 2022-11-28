package com.arsvechkarev.vault.features.common.di.modules

import buisnesslogic.FileSaver
import com.arsvechkarev.vault.features.common.data.FileReader
import com.arsvechkarev.vault.features.common.data.FileReaderImpl
import com.arsvechkarev.vault.features.common.data.FileSaverImpl
import com.arsvechkarev.vault.features.common.data.FilenameFromUriRetriever
import com.arsvechkarev.vault.features.common.data.FilenameFromUriRetrieverImpl

interface IoModule {
  val fileSaver: FileSaver
  val filenameFromUriRetriever: FilenameFromUriRetriever
  val fileReader: FileReader
}

class IoModuleImpl(
  coreModule: CoreModule
) : IoModule {
  
  override val fileSaver = FileSaverImpl(
    filename = "passwords.vault",
    coreModule.application,
    coreModule.dispatchersFacade
  )
  
  override val filenameFromUriRetriever = FilenameFromUriRetrieverImpl(coreModule.application)
  
  override val fileReader = FileReaderImpl(coreModule.application)
}
