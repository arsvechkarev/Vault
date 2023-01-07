package com.arsvechkarev.vault.features.common.di.modules

import buisnesslogic.AesSivTinkCryptography
import buisnesslogic.Cryptography
import buisnesslogic.base64.Base64Coder
import buisnesslogic.base64.JavaBase64Coder

interface CryptographyModule {
  val base64Coder: Base64Coder
  val cryptography: Cryptography
}

class CryptographyModuleImpl : CryptographyModule {
  override val base64Coder = JavaBase64Coder
  override val cryptography = AesSivTinkCryptography
}
