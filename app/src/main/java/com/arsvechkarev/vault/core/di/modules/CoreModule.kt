package com.arsvechkarev.vault.core.di.modules

import android.content.Context
import com.arsvechkarev.vault.core.AndroidThreader
import com.arsvechkarev.vault.core.Threader
import dagger.Module
import dagger.Provides
import navigation.Router
import javax.inject.Singleton

@Module
class CoreModule(private val context: Context) {
  
  @Provides
  @Singleton
  fun provideThreader(): Threader = AndroidThreader
  
  @Provides
  @Singleton
  fun provideContext(): Context = context
}