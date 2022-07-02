package com.arsvechkarev.vault.core.di.modules

import androidx.security.crypto.MasterKey
import buisnesslogic.FileSaver
import com.arsvechkarev.vault.core.EncryptionFileSaver
import com.arsvechkarev.vault.core.PASSWORDS_FILENAME

interface FileSaverModule {
  val fileSaver: FileSaver
}

class FileSaverModuleImpl(
  coreModule: CoreModule
) : FileSaverModule {
  
  private val masterKey = MasterKey.Builder(coreModule.application)
      .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
      .build()
  
  override val fileSaver =
      EncryptionFileSaver(PASSWORDS_FILENAME, coreModule.application, masterKey)
}
