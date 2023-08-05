package com.arsvechkarev.vault.features.common.di

import android.app.Application
import com.arsvechkarev.vault.features.common.data.ExternalFileReader
import com.arsvechkarev.vault.features.common.data.PasswordsFileExporter
import com.arsvechkarev.vault.features.common.di.modules.CoreModule
import com.arsvechkarev.vault.features.common.di.modules.CoreModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.CryptographyModule
import com.arsvechkarev.vault.features.common.di.modules.CryptographyModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.IoModule
import com.arsvechkarev.vault.features.common.di.modules.IoModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.NavigationModule
import com.arsvechkarev.vault.features.common.di.modules.NavigationModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.PasswordsModule
import com.arsvechkarev.vault.features.common.di.modules.PasswordsModuleImpl
import com.arsvechkarev.vault.features.common.navigation.ActivityResultSubstitutor

interface CoreComponent :
  CoreModule,
  CryptographyModule,
  IoModule,
  PasswordsModule,
  NavigationModule {
  
  companion object {
    
    fun create(
      application: Application,
      activityResultSubstitutor: ActivityResultSubstitutor,
      passwordsFileExporter: PasswordsFileExporter,
      externalFileReader: ExternalFileReader,
    ): CoreComponent {
      val coreModule = CoreModuleImpl(application)
      val cryptographyModule = CryptographyModuleImpl()
      val fileSaverModule = IoModuleImpl(coreModule, externalFileReader, passwordsFileExporter)
      val passwordsModule = PasswordsModuleImpl(coreModule, cryptographyModule, fileSaverModule)
      return CoreComponentImpl(coreModule, cryptographyModule, fileSaverModule,
        passwordsModule, NavigationModuleImpl(activityResultSubstitutor))
    }
  }
}

class CoreComponentImpl(
  private val coreModule: CoreModule,
  private val cryptographyModule: CryptographyModule,
  private val ioModule: IoModule,
  private val passwordsModule: PasswordsModule,
  private val navigationModule: NavigationModule,
) : CoreComponent,
  CoreModule by coreModule,
  CryptographyModule by cryptographyModule,
  IoModule by ioModule,
  PasswordsModule by passwordsModule,
  NavigationModule by navigationModule
