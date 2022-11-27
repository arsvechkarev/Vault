package com.arsvechkarev.vault.features.common.domain

import buisnesslogic.MasterPasswordHolder

interface MasterPasswordProvider {
  
  fun provideMasterPassword(): String
}

object MasterPasswordProviderImpl : MasterPasswordProvider {
  
  override fun provideMasterPassword(): String {
    return MasterPasswordHolder.masterPassword
  }
}
