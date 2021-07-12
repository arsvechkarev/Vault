package com.arsvechkarev.vault.core.di.modules

import buisnesslogic.Cryptography
import buisnesslogic.CryptographyImpl
import buisnesslogic.base64.Base64Coder
import buisnesslogic.base64.JavaBase64Coder
import buisnesslogic.random.SeedRandomGenerator
import buisnesslogic.random.SeedRandomGeneratorImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object CryptographyModule {
  
  @Provides
  @JvmStatic
  fun provideBase64Coder(): Base64Coder = JavaBase64Coder
  
  @Provides
  @JvmStatic
  fun provideSeedRandomGenerator(): SeedRandomGenerator = SeedRandomGeneratorImpl
  
  @Provides
  @JvmStatic
  @Singleton
  fun provideCryptography(
    base64Coder: Base64Coder,
    seedRandomGenerator: SeedRandomGenerator
  ): Cryptography {
    return CryptographyImpl(base64Coder, seedRandomGenerator)
  }
}