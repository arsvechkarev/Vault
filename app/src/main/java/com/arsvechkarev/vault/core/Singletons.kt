package com.arsvechkarev.vault.core

import android.content.Context
import androidx.security.crypto.MasterKey
import com.arsvechkarev.vault.cryptography.Cryptography
import com.arsvechkarev.vault.cryptography.JavaBase64Coder
import com.arsvechkarev.vault.cryptography.MasterPasswordChecker
import com.arsvechkarev.vault.cryptography.MasterPasswordCheckerImpl
import com.arsvechkarev.vault.cryptography.PasswordChecker
import com.arsvechkarev.vault.cryptography.SeedRandomGeneratorImpl
import com.arsvechkarev.vault.cryptography.ServicesInfoStorageImpl
import com.arsvechkarev.vault.cryptography.ZxcvbnPasswordChecker
import com.arsvechkarev.vault.cryptography.generator.PasswordGenerator
import com.arsvechkarev.vault.cryptography.generator.PasswordGeneratorImpl
import com.arsvechkarev.vault.features.common.ServicesRepository
import com.arsvechkarev.vault.features.common.UserAuthSaver
import com.arsvechkarev.vault.features.common.UserAuthSaverImpl
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingPresenter
import com.nulabinc.zxcvbn.Zxcvbn
import java.security.SecureRandom

object Singletons {
  
  private val passwordGenerator: PasswordGenerator = PasswordGeneratorImpl(SecureRandom())
  
  private var _userAuthSaver: UserAuthSaver? = null
  private var _masterPasswordChecker: MasterPasswordChecker? = null
  private var _servicesRepository: ServicesRepository? = null
  private var _masterKey: MasterKey? = null
  
  val passwordChecker: PasswordChecker = ZxcvbnPasswordChecker(Zxcvbn())
  val passwordCreatingPresenter = PasswordCreatingPresenter(passwordChecker, passwordGenerator)
  val userAuthSaver: UserAuthSaver get() = _userAuthSaver!!
  val masterPasswordChecker: MasterPasswordChecker get() = _masterPasswordChecker!!
  val servicesRepository: ServicesRepository get() = _servicesRepository!!
  val masterKey: MasterKey get() = _masterKey!!
  
  fun init(context: Context) {
    val cryptography = Cryptography(JavaBase64Coder, SeedRandomGeneratorImpl)
    val fileSaver = EncryptionFileSaver(context)
    val storage = ServicesInfoStorageImpl(cryptography, fileSaver, AndroidJsonConverter)
    _userAuthSaver = UserAuthSaverImpl(context)
    _masterPasswordChecker = MasterPasswordCheckerImpl(cryptography, fileSaver)
    _servicesRepository = ServicesRepository(storage, AndroidThreader)
    _masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
  }
}