package com.arsvechkarev.vault.core.di.modules

import buisnesslogic.AesSivTinkCryptography
import buisnesslogic.Cryptography
import buisnesslogic.base64.Base64Coder
import buisnesslogic.base64.JavaBase64Coder
import buisnesslogic.random.SeedRandomGenerator
import buisnesslogic.random.SeedRandomGeneratorImpl
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

    @Provides
    @JvmStatic
    fun provideCryptography(): Cryptography {
        return AesSivTinkCryptography
    }
}