package com.arsvechkarev.vault.core.di.modules

import buisnesslogic.AesSivTinkCryptography
import buisnesslogic.Cryptography
import buisnesslogic.base64.Base64Coder
import buisnesslogic.base64.JavaBase64Coder
import buisnesslogic.random.SeedRandomGenerator
import buisnesslogic.random.SeedRandomGeneratorImpl

interface CryptographyModule {
  val base64Coder: Base64Coder
  val seedRandomGenerator: SeedRandomGenerator
  val cryptography: Cryptography
}

class CryptographyModuleImpl : CryptographyModule {
  override val base64Coder = JavaBase64Coder
  override val seedRandomGenerator = SeedRandomGeneratorImpl
  override val cryptography = AesSivTinkCryptography
}
