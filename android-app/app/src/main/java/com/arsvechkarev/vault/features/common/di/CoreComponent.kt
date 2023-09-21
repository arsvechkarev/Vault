package com.arsvechkarev.vault.features.common.di

import android.app.Application
import com.arsvechkarev.vault.features.common.data.ExternalFileReader
import com.arsvechkarev.vault.features.common.data.PasswordsFileExporter
import com.arsvechkarev.vault.features.common.di.modules.CoreModule
import com.arsvechkarev.vault.features.common.di.modules.CoreModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.DomainModule
import com.arsvechkarev.vault.features.common.di.modules.DomainModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.IoModule
import com.arsvechkarev.vault.features.common.di.modules.IoModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.NavigationModule
import com.arsvechkarev.vault.features.common.di.modules.NavigationModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.PasswordsModule
import com.arsvechkarev.vault.features.common.di.modules.PasswordsModuleImpl
import com.arsvechkarev.vault.features.common.navigation.ActivityResultSubstitutor

interface CoreComponent :
  CoreModule,
  DomainModule,
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
      val ioModule = IoModuleImpl(coreModule, externalFileReader, passwordsFileExporter)
      val domainModule = DomainModuleImpl(ioModule)
      val passwordsModule = PasswordsModuleImpl(ioModule, domainModule)
      return CoreComponentImpl(coreModule, domainModule, ioModule,
        passwordsModule, NavigationModuleImpl(activityResultSubstitutor))
    }
  }
}

class CoreComponentImpl(
  private val coreModule: CoreModule,
  private val domainModule: DomainModule,
  private val ioModule: IoModule,
  private val passwordsModule: PasswordsModule,
  private val navigationModule: NavigationModule,
) : CoreComponent,
  CoreModule by coreModule,
  DomainModule by domainModule,
  IoModule by ioModule,
  PasswordsModule by passwordsModule,
  NavigationModule by navigationModule
