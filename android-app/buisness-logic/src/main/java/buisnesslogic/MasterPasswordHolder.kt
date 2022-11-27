package buisnesslogic

// TODO (7/21/2022): Store password as byte array?
object MasterPasswordHolder {
  
  private var _masterPassword: String? = null
  
  val masterPassword: String get() = _masterPassword!!
  
  fun setMasterPassword(masterPassword: String) {
    _masterPassword = masterPassword
  }
}
