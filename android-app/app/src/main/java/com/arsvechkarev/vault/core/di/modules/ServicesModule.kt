package com.arsvechkarev.vault.core.di.modules

import buisnesslogic.Cryptography
import buisnesslogic.FileSaver
import buisnesslogic.GsonJsonConverter
import buisnesslogic.JsonConverter
import buisnesslogic.ServicesStorage
import buisnesslogic.ServicesStorageImpl
import com.arsvechkarev.vault.common.ServicesListenableRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [CryptographyModule::class, CoreModule::class, FileSaverModule::class])
object ServicesModule {
  
  @Provides
  @JvmStatic
  fun provideJsonConverter(): JsonConverter = GsonJsonConverter
  
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
  fun provideServicesListenableRepository(
    servicesStorage: ServicesStorage
  ): ServicesListenableRepository {
    return ServicesListenableRepository(servicesStorage)
  }
}