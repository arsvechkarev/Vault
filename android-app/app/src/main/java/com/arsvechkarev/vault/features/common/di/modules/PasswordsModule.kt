package com.arsvechkarev.vault.features.common.di.modules

import buisnesslogic.MasterPasswordChecker
import buisnesslogic.MasterPasswordCheckerImpl
import buisnesslogic.PasswordCharacteristicsProvider
import buisnesslogic.PasswordCharacteristicsProviderImpl
import buisnesslogic.PasswordChecker
import buisnesslogic.ZxcvbnPasswordChecker
import buisnesslogic.generator.NumbersGenerator
import buisnesslogic.generator.PasswordGenerator
import buisnesslogic.generator.PasswordGeneratorImpl
import buisnesslogic.generator.SecureRandomGenerator
import buisnesslogic.generator.SpecialSymbolsGenerator
import buisnesslogic.generator.UppercaseSymbolsGenerator
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProviderImpl
import com.nulabinc.zxcvbn.Zxcvbn

interface PasswordsModule {
  val passwordChecker: PasswordChecker
  val passwordGenerator: PasswordGenerator
  val passwordCharacteristicsProvider: PasswordCharacteristicsProvider
  val masterPasswordChecker: MasterPasswordChecker
  val masterPasswordProvider: MasterPasswordProvider
}

class PasswordsModuleImpl(
  keePassModule: KeePassModule,
) : PasswordsModule {
  
  override val passwordChecker = ZxcvbnPasswordChecker(Zxcvbn())
  
  override val passwordGenerator = PasswordGeneratorImpl(SecureRandomGenerator)
  
  override val passwordCharacteristicsProvider = PasswordCharacteristicsProviderImpl(
    UppercaseSymbolsGenerator(SecureRandomGenerator),
    NumbersGenerator(SecureRandomGenerator),
    SpecialSymbolsGenerator(SecureRandomGenerator),
  )
  
  override val masterPasswordProvider = MasterPasswordProviderImpl
  
  override val masterPasswordChecker =
      MasterPasswordCheckerImpl(keePassModule.databaseFileSaver, keePassModule.databaseCache)
}
