package com.arsvechkarev.vault.core.di.modules

import android.content.Context
import androidx.security.crypto.MasterKey
import buisnesslogic.FileSaver
import com.arsvechkarev.vault.core.EncryptionFileSaver
import com.arsvechkarev.vault.core.PASSWORDS_FILENAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [CoreModule::class, MasterKeyModule::class])
object FileSaverModule {
  
  @Provides
  @JvmStatic
  @Singleton
  fun provideFileSaver(
    context: Context,
    masterKey: MasterKey
  ): FileSaver {
    return EncryptionFileSaver(PASSWORDS_FILENAME, context, masterKey)
  }
}