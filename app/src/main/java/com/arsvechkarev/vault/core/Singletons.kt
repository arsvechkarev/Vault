package com.arsvechkarev.vault.core

import android.content.Context
import androidx.security.crypto.MasterKey
import com.arsvechkarev.vault.cryptography.MasterPasswordChecker
import com.arsvechkarev.vault.cryptography.MasterPasswordCheckerImpl
import com.arsvechkarev.vault.cryptography.MasterPasswordSaver
import com.arsvechkarev.vault.cryptography.MasterPasswordSaverImpl
import com.arsvechkarev.vault.cryptography.PasswordChecker
import com.arsvechkarev.vault.cryptography.PasswordsStorageImpl
import com.arsvechkarev.vault.cryptography.ZxcvbnPasswordChecker
import com.arsvechkarev.vault.cryptography.generator.PasswordGenerator
import com.arsvechkarev.vault.cryptography.generator.PasswordGeneratorImpl
import com.arsvechkarev.vault.features.common.PasswordsListCachingRepository
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingPresenter
import com.nulabinc.zxcvbn.Zxcvbn
import java.security.SecureRandom

object Singletons {
  
  private val random = SecureRandom()
  private val passwordGenerator: PasswordGenerator = PasswordGeneratorImpl(random)
  
  private var _masterPasswordSaver: MasterPasswordSaver? = null
  private var _userAuthSaver: UserAuthSaver? = null
  private var _masterPasswordChecker: MasterPasswordChecker? = null
  private var _passwordsListCachingRepository: PasswordsListCachingRepository? = null
  private var _masterKey: MasterKey? = null
  
  val passwordChecker: PasswordChecker = ZxcvbnPasswordChecker(Zxcvbn())
  val passwordCreatingPresenter = PasswordCreatingPresenter(passwordChecker, passwordGenerator)
  
  val masterPasswordSaver: MasterPasswordSaver
    get() = _masterPasswordSaver!!
  
  val userAuthSaver: UserAuthSaver
    get() = _userAuthSaver!!
  
  val masterPasswordChecker: MasterPasswordChecker
    get() = _masterPasswordChecker!!
  
  val passwordsListCachingRepository: PasswordsListCachingRepository
    get() = _passwordsListCachingRepository!!
  
  val masterKey: MasterKey
    get() = _masterKey!!
  
  fun init(context: Context) {
    _masterPasswordSaver = MasterPasswordSaverImpl(context)
    _userAuthSaver = UserAuthSaverImpl(context)
    _masterPasswordChecker = MasterPasswordCheckerImpl(context)
    _passwordsListCachingRepository = PasswordsListCachingRepository(PasswordsStorageImpl(context))
    _masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
  }
}