package com.arsvechkarev.vault.core.di.modules

import com.arsvechkarev.vault.cryptography.Base64Coder
import com.arsvechkarev.vault.cryptography.JavaBase64Coder
import com.arsvechkarev.vault.cryptography.SeedRandomGenerator
import com.arsvechkarev.vault.cryptography.SeedRandomGeneratorImpl
import dagger.Module
import dagger.Provides

@Module
object CryptographyModule {
  
  @Provides
  @JvmStatic
  fun provideBase64Coder(): Base64Coder = JavaBase64Coder
  
  @Provides
  @JvmStatic
  fun provideSeedRandomGenerator(): SeedRandomGenerator = SeedRandomGeneratorImpl
}