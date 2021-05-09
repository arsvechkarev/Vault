package com.arsvechkarev.vault.core.di.modules

import android.content.Context
import com.arsvechkarev.vault.features.common.AndroidFingerprintsChecker
import com.arsvechkarev.vault.features.common.FingerprintsChecker
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [CoreModule::class])
object FingerprintsCheckerModule {
  
  @Provides
  @JvmStatic
  @Singleton
  fun provideFingerprintsChecker(context: Context): FingerprintsChecker {
    return AndroidFingerprintsChecker(context)
  }
}