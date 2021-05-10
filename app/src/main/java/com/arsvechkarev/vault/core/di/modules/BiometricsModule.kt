package com.arsvechkarev.vault.core.di.modules

import android.content.Context
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.security.crypto.MasterKey
import buisnesslogic.base64.Base64Coder
import com.arsvechkarev.vault.core.di.ApplicationScope
import com.arsvechkarev.vault.features.common.EncryptionFileSaver
import com.arsvechkarev.vault.features.common.biometrics.BiometricsAvailabilityChecker
import com.arsvechkarev.vault.features.common.biometrics.BiometricsAvailabilityCheckerImpl
import com.arsvechkarev.vault.features.common.biometrics.BiometricsPrompt
import com.arsvechkarev.vault.features.common.biometrics.BiometricsPromptImpl
import com.arsvechkarev.vault.features.common.biometrics.BiometricsStorage
import com.arsvechkarev.vault.features.common.biometrics.BiometricsStorage.Companion.BIOMETRICS_IV_FILENAME
import com.arsvechkarev.vault.features.common.biometrics.BiometricsStorage.Companion.BIOMETRICS_PASSWORD_FILENAME
import com.arsvechkarev.vault.features.common.biometrics.SavedFingerprintChecker
import com.arsvechkarev.vault.features.common.biometrics.SavedFingerprintCheckerImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module(includes = [
  CoreModule::class,
  MasterKeyModule::class,
  ActivityModule::class,
  CryptographyModule::class
])
object BiometricsModule {
  
  @Provides
  @JvmStatic
  @Singleton
  fun provideFingerprintsAvailabilityChecker(context: Context): BiometricsAvailabilityChecker {
    return BiometricsAvailabilityCheckerImpl(context)
  }
  
  @Provides
  @JvmStatic
  @Singleton
  fun provideSavedFingerprintsChecker(context: Context): SavedFingerprintChecker {
    return SavedFingerprintCheckerImpl(context)
  }
  
  @Provides
  @JvmStatic
  @Singleton
  @RequiresApi(28)
  fun provideBiometricsPrompt(
    activity: FragmentActivity,
    @ApplicationScope coroutineScope: CoroutineScope
  ): BiometricsPrompt {
    return BiometricsPromptImpl(activity, coroutineScope)
  }
  
  @Provides
  @JvmStatic
  @Singleton
  @RequiresApi(28)
  fun provideBiometricsStorage(
    context: Context,
    masterKey: MasterKey,
    base64Coder: Base64Coder
  ): BiometricsStorage {
    val passwordFileSaver = EncryptionFileSaver(BIOMETRICS_PASSWORD_FILENAME, context, masterKey)
    val ivFileSaver = EncryptionFileSaver(BIOMETRICS_IV_FILENAME, context, masterKey)
    return BiometricsStorage(passwordFileSaver, ivFileSaver, base64Coder)
  }
}