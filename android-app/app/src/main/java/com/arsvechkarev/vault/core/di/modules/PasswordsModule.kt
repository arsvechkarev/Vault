package com.arsvechkarev.vault.core.di.modules

import buisnesslogic.IdGeneratorImpl
import buisnesslogic.MasterPasswordChecker
import buisnesslogic.MasterPasswordCheckerImpl
import buisnesslogic.PasswordInfoChecker
import buisnesslogic.PasswordsStorageImpl
import buisnesslogic.ZxcvbnPasswordInfoChecker
import buisnesslogic.generator.PasswordGenerator
import buisnesslogic.generator.PasswordGeneratorImpl
import com.arsvechkarev.vault.core.CachedPasswordsStorage
import com.arsvechkarev.vault.core.ListenableCachedPasswordStorage
import com.arsvechkarev.vault.core.MasterPasswordProvider
import com.arsvechkarev.vault.core.MasterPasswordProviderImpl
import com.nulabinc.zxcvbn.Zxcvbn
import java.security.SecureRandom

interface PasswordsModule {
  val passwordInfoChecker: PasswordInfoChecker
  val passwordGenerator: PasswordGenerator
  val masterPasswordChecker: MasterPasswordChecker
  val masterPasswordProvider: MasterPasswordProvider
  val listenableCachedPasswordStorage: ListenableCachedPasswordStorage
}

class PasswordsModuleImpl(
  coreModule: CoreModule,
  cryptographyModule: CryptographyModule,
  fileSaverModule: FileSaverModule,
) : PasswordsModule {
  
  private val secureRandom = SecureRandom()
  
  override val passwordInfoChecker = ZxcvbnPasswordInfoChecker(Zxcvbn())
  override val passwordGenerator = PasswordGeneratorImpl { secureRandom.nextInt(it) }
  override val masterPasswordProvider = MasterPasswordProviderImpl
  override val masterPasswordChecker =
      MasterPasswordCheckerImpl(cryptographyModule.cryptography, fileSaverModule.fileSaver)
  
  override val listenableCachedPasswordStorage = ListenableCachedPasswordStorage(
    CachedPasswordsStorage(
      storage = PasswordsStorageImpl(
        cryptographyModule.cryptography,
        fileSaverModule.fileSaver,
        coreModule.jsonConverter
      ),
      idGenerator = IdGeneratorImpl
    )
  )
}