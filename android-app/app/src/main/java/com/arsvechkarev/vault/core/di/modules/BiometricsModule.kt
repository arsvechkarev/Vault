package com.arsvechkarev.vault.core.di.modules

import android.content.Context
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.security.crypto.MasterKey
import buisnesslogic.base64.Base64Coder
import com.arsvechkarev.vault.core.di.ApplicationScope
import com.arsvechkarev.vault.features.common.EncryptionFileSaver
import com.arsvechkarev.vault.features.common.biometrics.*
import com.arsvechkarev.vault.features.common.biometrics.BiometricsStorage.Companion.BIOMETRICS_IV_FILENAME
import com.arsvechkarev.vault.features.common.biometrics.BiometricsStorage.Companion.BIOMETRICS_PASSWORD_FILENAME
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module(
    includes = [
        CoreModule::class,
        MasterKeyModule::class,
        ActivityModule::class,
        CryptographyModule::class
    ]
)
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
    @RequiresApi(28)
    fun provideBiometricsPrompt(
        appContext: Context,
        activity: FragmentActivity,
        @ApplicationScope coroutineScope: CoroutineScope
    ): BiometricsPrompt {
        return BiometricsPromptImpl(appContext, activity, coroutineScope)
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
        val passwordFileSaver =
            EncryptionFileSaver(BIOMETRICS_PASSWORD_FILENAME, context, masterKey)
        val ivFileSaver = EncryptionFileSaver(BIOMETRICS_IV_FILENAME, context, masterKey)
        return BiometricsStorage(passwordFileSaver, ivFileSaver, base64Coder)
    }
}