package com.arsvechkarev.vault.test.core.di.stubs

import com.arsvechkarev.vault.features.common.data.files.KeyFileSaver

class StubKeyFileSaver : KeyFileSaver {
  
  private var keyFileData: ByteArray? = null
  
  override suspend fun saveKeyFile(bytes: ByteArray) {
    keyFileData = bytes
  }
  
  override suspend fun getKeyFileIfExists(): ByteArray? {
    return keyFileData
  }
}
