package com.arsvechkarev.vault.features.common.di

import android.app.Application
import com.arsvechkarev.vault.features.common.di.modules.AuthModule
import com.arsvechkarev.vault.features.common.di.modules.AuthModuleImpl
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

interface CoreComponent :
  CoreModule,
  CryptographyModule,
  IoModule,
  PasswordsModule,
  AuthModule,
  NavigationModule {
  
  companion object {
    
    fun create(application: Application): CoreComponent {
      val coreModule = CoreModuleImpl(application)
      val cryptographyModule = CryptographyModuleImpl()
      val fileSaverModule = IoModuleImpl(coreModule)
      val passwordsModule = PasswordsModuleImpl(coreModule, cryptographyModule, fileSaverModule)
      val authModule = AuthModuleImpl(coreModule)
      return CoreComponentImpl(coreModule, cryptographyModule, fileSaverModule,
        passwordsModule, authModule, NavigationModuleImpl())
    }
  }
}

class CoreComponentImpl(
  private val coreModule: CoreModule,
  private val cryptographyModule: CryptographyModule,
  private val ioModule: IoModule,
  private val passwordsModule: PasswordsModule,
  private val authModule: AuthModule,
  private val navigationModule: NavigationModule,
) : CoreComponent,
  CoreModule by coreModule,
  CryptographyModule by cryptographyModule,
  IoModule by ioModule,
  PasswordsModule by passwordsModule,
  AuthModule by authModule,
  NavigationModule by navigationModule
