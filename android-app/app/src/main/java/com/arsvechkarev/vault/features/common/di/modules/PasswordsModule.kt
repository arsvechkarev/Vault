package com.arsvechkarev.vault.features.common.di.modules

import buisnesslogic.IdGeneratorImpl
import buisnesslogic.MasterPasswordChecker
import buisnesslogic.MasterPasswordCheckerImpl
import buisnesslogic.PasswordInfoChecker
import buisnesslogic.PasswordsStorageImpl
import buisnesslogic.ZxcvbnPasswordInfoChecker
import buisnesslogic.generator.PasswordGenerator
import buisnesslogic.generator.PasswordGeneratorImpl
import buisnesslogic.generator.SecureRandomGenerator
import com.arsvechkarev.vault.features.common.data.storage.CachedPasswordsStorage
import com.arsvechkarev.vault.features.common.data.storage.ListenableCachedPasswordsStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProviderImpl
import com.nulabinc.zxcvbn.Zxcvbn

interface PasswordsModule {
  val passwordInfoChecker: PasswordInfoChecker
  val passwordGenerator: PasswordGenerator
  val masterPasswordChecker: MasterPasswordChecker
  val masterPasswordProvider: MasterPasswordProvider
  val listenableCachedPasswordsStorage: ListenableCachedPasswordsStorage
}

class PasswordsModuleImpl(
  coreModule: CoreModule,
  cryptographyModule: CryptographyModule,
  ioModule: IoModule,
) : PasswordsModule {
  
  override val passwordInfoChecker = ZxcvbnPasswordInfoChecker(Zxcvbn())
  override val passwordGenerator = PasswordGeneratorImpl(SecureRandomGenerator)
  override val masterPasswordProvider = MasterPasswordProviderImpl
  override val masterPasswordChecker =
      MasterPasswordCheckerImpl(cryptographyModule.cryptography, ioModule.fileSaver)
  
  override val listenableCachedPasswordsStorage = ListenableCachedPasswordsStorage(
    CachedPasswordsStorage(
      storage = PasswordsStorageImpl(
        cryptographyModule.cryptography,
        ioModule.fileSaver,
        coreModule.gson,
      ),
      idGenerator = IdGeneratorImpl
    )
  )
}
