package com.arsvechkarev.vault.core.di.modules

import buisnesslogic.*
import buisnesslogic.generator.PasswordGenerator
import buisnesslogic.generator.PasswordGeneratorImpl
import com.nulabinc.zxcvbn.Zxcvbn
import dagger.Module
import dagger.Provides
import java.security.SecureRandom
import javax.inject.Singleton

@Module(includes = [CryptographyModule::class, FileSaverModule::class])
object PasswordActionsModule {

    @Provides
    @JvmStatic
    fun provideZxcvbn() = Zxcvbn()

    @Provides
    @JvmStatic
    fun provideSecureRandom() = SecureRandom()

    @Provides
    @JvmStatic
    @Singleton
    fun providePasswordChecker(zxcvbn: Zxcvbn): PasswordChecker = ZxcvbnPasswordChecker(zxcvbn)

    @Provides
    @JvmStatic
    @Singleton
    fun providePasswordGenerator(secureRandom: SecureRandom): PasswordGenerator {
        return PasswordGeneratorImpl(secureRandom)
    }

    @Provides
    @JvmStatic
    @Singleton
    fun provideMasterPasswordChecker(
        cryptography: Cryptography,
        fileSaver: FileSaver
    ): MasterPasswordChecker {
        return MasterPasswordCheckerImpl(cryptography, fileSaver)
    }
}