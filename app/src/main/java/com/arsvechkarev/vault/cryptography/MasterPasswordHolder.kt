package com.arsvechkarev.vault.cryptography

object MasterPasswordHolder {
  
  private var _masterPassword: String? = null
  
  val masterPassword: String
    get() = _masterPassword!!
  
  val isMasterPasswordSet: Boolean
    get() = _masterPassword != null
  
  fun setMasterPassword(masterPassword: String) {
    _masterPassword = masterPassword
  }
  
  fun clearMasterPassword() {
    _masterPassword = null
  }
}