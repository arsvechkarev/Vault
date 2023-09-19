package com.arsvechkarev.vault.features.common.di.modules

import buisnesslogic.EntriesStorageImpl
import buisnesslogic.IdGeneratorImpl
import buisnesslogic.MasterPasswordChecker
import buisnesslogic.MasterPasswordCheckerImpl
import buisnesslogic.PasswordCharacteristicsProvider
import buisnesslogic.PasswordCharacteristicsProviderImpl
import buisnesslogic.PasswordInfoChecker
import buisnesslogic.ZxcvbnPasswordInfoChecker
import buisnesslogic.generator.NumbersGenerator
import buisnesslogic.generator.PasswordGenerator
import buisnesslogic.generator.PasswordGeneratorImpl
import buisnesslogic.generator.SecureRandomGenerator
import buisnesslogic.generator.SpecialSymbolsGenerator
import buisnesslogic.generator.UppercaseSymbolsGenerator
import com.arsvechkarev.vault.features.common.data.storage.CachedEntriesStorage
import com.arsvechkarev.vault.features.common.data.storage.ListenableCachedEntriesStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProviderImpl
import com.nulabinc.zxcvbn.Zxcvbn

interface PasswordsModule {
  val passwordInfoChecker: PasswordInfoChecker
  val passwordGenerator: PasswordGenerator
  val passwordCharacteristicsProvider: PasswordCharacteristicsProvider
  val masterPasswordChecker: MasterPasswordChecker
  val masterPasswordProvider: MasterPasswordProvider
  val listenableCachedEntriesStorage: ListenableCachedEntriesStorage
}

class PasswordsModuleImpl(
  coreModule: CoreModule,
  cryptographyModule: CryptographyModule,
  ioModule: IoModule,
) : PasswordsModule {
  
  override val passwordInfoChecker = ZxcvbnPasswordInfoChecker(Zxcvbn())
  
  override val passwordGenerator = PasswordGeneratorImpl(SecureRandomGenerator)
  
  override val passwordCharacteristicsProvider = PasswordCharacteristicsProviderImpl(
    UppercaseSymbolsGenerator(SecureRandomGenerator),
    NumbersGenerator(SecureRandomGenerator),
    SpecialSymbolsGenerator(SecureRandomGenerator),
  )
  
  override val masterPasswordProvider = MasterPasswordProviderImpl
  
  override val masterPasswordChecker =
      MasterPasswordCheckerImpl(ioModule.databaseSaver)
  
  override val listenableCachedEntriesStorage = ListenableCachedEntriesStorage(
    CachedEntriesStorage(
      storage = EntriesStorageImpl(
        cryptographyModule.cryptography,
        ioModule.databaseSaver,
        coreModule.gson,
      ),
      idGenerator = IdGeneratorImpl
    )
  )
}
