package com.arsvechkarev.vault.core.di.modules

import buisnesslogic.MasterPasswordChecker
import buisnesslogic.MasterPasswordCheckerImpl
import buisnesslogic.PasswordChecker
import buisnesslogic.ZxcvbnPasswordChecker
import buisnesslogic.generator.PasswordGenerator
import buisnesslogic.generator.PasswordGeneratorImpl
import com.nulabinc.zxcvbn.Zxcvbn
import java.security.SecureRandom

interface PasswordActionsModule {
  val passwordChecker: PasswordChecker
  val passwordGenerator: PasswordGenerator
  val masterPasswordChecker: MasterPasswordChecker
}

class PasswordActionsModuleImpl(
  cryptographyModule: CryptographyModule,
  fileSaverModule: FileSaverModule,
) : PasswordActionsModule {
  
  private val secureRandom = SecureRandom()
  
  override val passwordChecker = ZxcvbnPasswordChecker(Zxcvbn())
  override val passwordGenerator = PasswordGeneratorImpl { secureRandom.nextInt(it) }
  override val masterPasswordChecker =
      MasterPasswordCheckerImpl(cryptographyModule.cryptography, fileSaverModule.fileSaver)
}