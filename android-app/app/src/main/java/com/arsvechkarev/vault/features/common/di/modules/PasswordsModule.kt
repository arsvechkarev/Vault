package com.arsvechkarev.vault.features.common.di.modules

import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProviderImpl
import com.nulabinc.zxcvbn.Zxcvbn
import domain.MasterPasswordChecker
import domain.MasterPasswordCheckerImpl
import domain.PasswordCharacteristicsProvider
import domain.PasswordCharacteristicsProviderImpl
import domain.PasswordChecker
import domain.ZxcvbnPasswordChecker
import domain.generator.NumbersGenerator
import domain.generator.PasswordGenerator
import domain.generator.PasswordGeneratorImpl
import domain.generator.SecureRandomGenerator
import domain.generator.SpecialSymbolsGenerator
import domain.generator.UppercaseSymbolsGenerator

interface PasswordsModule {
  val passwordChecker: PasswordChecker
  val passwordGenerator: PasswordGenerator
  val passwordCharacteristicsProvider: PasswordCharacteristicsProvider
  val masterPasswordChecker: MasterPasswordChecker
  val masterPasswordProvider: MasterPasswordProvider
}

class PasswordsModuleImpl(
  domainModule: DomainModule,
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
      MasterPasswordCheckerImpl(domainModule.databaseFileSaver, domainModule.databaseCache)
}
