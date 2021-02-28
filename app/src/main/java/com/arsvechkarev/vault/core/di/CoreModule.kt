package com.arsvechkarev.vault.core.di

import android.content.Context
import com.arsvechkarev.vault.core.AndroidThreader
import com.arsvechkarev.vault.core.Threader
import dagger.Module
import dagger.Provides

@Module
class CoreModule(private val context: Context) {
  
  @Provides
  fun provideThreader(): Threader = AndroidThreader
  
  @Provides
  fun provideContext(): Context = context
}