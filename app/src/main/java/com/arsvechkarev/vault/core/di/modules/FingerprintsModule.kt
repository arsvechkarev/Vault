package com.arsvechkarev.vault.core.di.modules

import android.content.Context
import com.arsvechkarev.vault.features.common.fingerprints.FingerprintsAvailabilityChecker
import com.arsvechkarev.vault.features.common.fingerprints.FingerprintsAvailabilityCheckerImpl
import com.arsvechkarev.vault.features.common.fingerprints.SavedFingerprintChecker
import com.arsvechkarev.vault.features.common.fingerprints.SavedFingerprintCheckerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [CoreModule::class])
object FingerprintsModule {
  
  @Provides
  @JvmStatic
  @Singleton
  fun provideFingerprintsAvailabilityChecker(context: Context): FingerprintsAvailabilityChecker {
    return FingerprintsAvailabilityCheckerImpl(context)
  }
  
  @Provides
  @JvmStatic
  @Singleton
  fun provideSavedFingerprintsChecker(context: Context): SavedFingerprintChecker {
    return SavedFingerprintCheckerImpl(context)
  }
}