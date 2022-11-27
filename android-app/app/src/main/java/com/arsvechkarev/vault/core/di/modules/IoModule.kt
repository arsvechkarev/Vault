package com.arsvechkarev.vault.core.di.modules

import androidx.security.crypto.MasterKey
import buisnesslogic.FileSaver
import com.arsvechkarev.vault.features.common.data.EncryptionFileSaver
import com.arsvechkarev.vault.features.common.data.FilenameFromUriRetriever
import com.arsvechkarev.vault.features.common.data.FilenameFromUriRetrieverImpl
import com.arsvechkarev.vault.features.common.domain.PASSWORDS_FILENAME

interface IoModule {
  val fileSaver: FileSaver
  val filenameFromUriRetriever: FilenameFromUriRetriever
}

class IoModuleImpl(
  coreModule: CoreModule
) : IoModule {
  
  private val masterKey = MasterKey.Builder(coreModule.application)
      .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
      .build()
  
  override val fileSaver = EncryptionFileSaver(
    PASSWORDS_FILENAME,
    coreModule.application,
    masterKey,
    coreModule.dispatchersFacade
  )
  
  override val filenameFromUriRetriever = FilenameFromUriRetrieverImpl(coreModule.application)
}
