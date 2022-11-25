package com.arsvechkarev.vault.core

import buisnesslogic.MasterPasswordHolder

interface MasterPasswordProvider {
  
  fun provideMasterPassword(): String
}

object MasterPasswordProviderImpl : MasterPasswordProvider {
  
  override fun provideMasterPassword(): String {
    return MasterPasswordHolder.masterPassword
  }
}
