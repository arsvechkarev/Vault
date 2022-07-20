package com.arsvechkarev.vault.core.di.modules

import buisnesslogic.MasterPasswordChecker
import buisnesslogic.MasterPasswordCheckerImpl
import buisnesslogic.PasswordInfoChecker
import buisnesslogic.ZxcvbnPasswordInfoChecker
import buisnesslogic.generator.PasswordGenerator
import buisnesslogic.generator.PasswordGeneratorImpl
import com.nulabinc.zxcvbn.Zxcvbn
import java.security.SecureRandom

interface PasswordActionsModule {
  val passwordInfoChecker: PasswordInfoChecker
  val passwordGenerator: PasswordGenerator
  val masterPasswordChecker: MasterPasswordChecker
}

class PasswordActionsModuleImpl(
  cryptographyModule: CryptographyModule,
  fileSaverModule: FileSaverModule,
) : PasswordActionsModule {
  
  private val secureRandom = SecureRandom()
  
  override val passwordInfoChecker = ZxcvbnPasswordInfoChecker(Zxcvbn())
  override val passwordGenerator = PasswordGeneratorImpl { secureRandom.nextInt(it) }
  override val masterPasswordChecker =
      MasterPasswordCheckerImpl(cryptographyModule.cryptography, fileSaverModule.fileSaver)
}