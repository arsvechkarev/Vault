package com.arsvechkarev.vault.features.common.domain

import buisnesslogic.MasterPasswordHolder
import buisnesslogic.Password

interface MasterPasswordProvider {
  
  fun provideMasterPassword(): Password = requireNotNull(provideMasterPasswordIfSet())
  
  fun provideMasterPasswordIfSet(): Password?
}

object MasterPasswordProviderImpl : MasterPasswordProvider {
  override fun provideMasterPasswordIfSet(): Password? {
    return MasterPasswordHolder.masterPassword
  }
}
