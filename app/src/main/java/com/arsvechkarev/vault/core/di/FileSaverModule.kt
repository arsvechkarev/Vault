package com.arsvechkarev.vault.core.di

import android.content.Context
import com.arsvechkarev.vault.core.EncryptionFileSaver
import com.arsvechkarev.vault.core.FileSaver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [CoreModule::class])
object FileSaverModule {
  
  @Provides
  @JvmStatic
  @Singleton
  fun provideFileSaver(context: Context): FileSaver = EncryptionFileSaver(context)
}