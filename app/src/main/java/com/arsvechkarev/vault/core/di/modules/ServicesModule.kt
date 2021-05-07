package com.arsvechkarev.vault.core.di.modules

import com.arsvechkarev.vault.core.AndroidJsonConverter
import com.arsvechkarev.vault.core.FileSaver
import com.arsvechkarev.vault.core.JsonConverter
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.cryptography.Cryptography
import com.arsvechkarev.vault.cryptography.ServicesStorage
import com.arsvechkarev.vault.cryptography.ServicesStorageImpl
import com.arsvechkarev.vault.features.common.ServicesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [CryptographyModule::class, CoreModule::class, FileSaverModule::class])
object ServicesModule {
  
  @Provides
  @JvmStatic
  fun provideJsonConverter(): JsonConverter = AndroidJsonConverter
  
  @Provides
  @JvmStatic
  @Singleton
  fun provideServicesStorage(
    cryptography: Cryptography,
    fileSaver: FileSaver,
    jsonConverter: JsonConverter
  ): ServicesStorage {
    return ServicesStorageImpl(cryptography, fileSaver, jsonConverter)
  }
  
  @Provides
  @JvmStatic
  @Singleton
  fun provideServicesRepository(
    servicesStorage: ServicesStorage,
    threader: Threader
  ): ServicesRepository {
    return ServicesRepository(servicesStorage, threader)
  }
}