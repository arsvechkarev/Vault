package com.arsvechkarev.vault.core

import android.content.Context
import com.arsvechkarev.vault.password.MasterPasswordChecker
import com.arsvechkarev.vault.password.MasterPasswordCheckerImpl
import com.arsvechkarev.vault.password.MasterPasswordSaver
import com.arsvechkarev.vault.password.MasterPasswordSaverImpl

object Singletons {
  
  private var _masterPasswordSaver: MasterPasswordSaver? = null
  private var _userAuthSaver: UserAuthSaver? = null
  private var _masterPasswordChecker: MasterPasswordChecker? = null
  
  val masterPasswordSaver: MasterPasswordSaver
    get() = _masterPasswordSaver!!
  
  val userAuthSaver: UserAuthSaver
    get() = _userAuthSaver!!
  
  val masterPasswordChecker: MasterPasswordChecker
    get() = _masterPasswordChecker!!
  
  fun init(context: Context) {
    _masterPasswordSaver = MasterPasswordSaverImpl(context)
    _userAuthSaver = UserAuthSaverImpl(context)
    _masterPasswordChecker = MasterPasswordCheckerImpl(context)
  }
}
