package com.arsvechkarev.vault.core.di.modules

import android.content.Context
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [CoreModule::class])
object MasterKeyModule {
  
  @Provides
  @Singleton
  @JvmStatic
  fun provideMasterKey(context: Context): MasterKey {
    return MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
  }
}