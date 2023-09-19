package com.arsvechkarev.vault.features.common.domain

import buisnesslogic.MasterPasswordHolder
import buisnesslogic.Password

interface MasterPasswordProvider {
  
  fun provideMasterPassword(): Password
}

object MasterPasswordProviderImpl : MasterPasswordProvider {
  
  override fun provideMasterPassword(): Password {
    return MasterPasswordHolder.masterPassword
  }
}
