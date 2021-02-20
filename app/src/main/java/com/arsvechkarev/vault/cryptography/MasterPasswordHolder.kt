package com.arsvechkarev.vault.cryptography

object MasterPasswordHolder {
  
  private var _masterPassword: String? = null
  
  val masterPassword: String get() = _masterPassword!!
  
  fun setMasterPassword(masterPassword: String) {
    _masterPassword = masterPassword
  }
}