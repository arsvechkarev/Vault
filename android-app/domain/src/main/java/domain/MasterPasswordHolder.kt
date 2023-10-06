package domain

object MasterPasswordHolder {
  
  private var _masterPassword: Password? = null
  
  val masterPassword: Password? get() = _masterPassword
  
  fun setMasterPassword(masterPassword: Password) {
    _masterPassword = masterPassword
  }
}
