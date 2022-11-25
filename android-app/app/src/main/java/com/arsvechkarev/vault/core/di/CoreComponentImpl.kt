package com.arsvechkarev.vault.core.di

import android.app.Application
import com.arsvechkarev.vault.core.di.modules.AuthModule
import com.arsvechkarev.vault.core.di.modules.AuthModuleImpl
import com.arsvechkarev.vault.core.di.modules.CoreModule
import com.arsvechkarev.vault.core.di.modules.CoreModuleImpl
import com.arsvechkarev.vault.core.di.modules.CryptographyModule
import com.arsvechkarev.vault.core.di.modules.CryptographyModuleImpl
import com.arsvechkarev.vault.core.di.modules.FileSaverModule
import com.arsvechkarev.vault.core.di.modules.FileSaverModuleImpl
import com.arsvechkarev.vault.core.di.modules.PasswordsModule
import com.arsvechkarev.vault.core.di.modules.PasswordsModuleImpl

interface CoreComponent :
  CoreModule,
  CryptographyModule,
  FileSaverModule,
  PasswordsModule,
  AuthModule {
  
  companion object {
    
    fun create(application: Application): CoreComponent {
      val coreModule = CoreModuleImpl(application)
      val cryptographyModule = CryptographyModuleImpl()
      val fileSaverModule = FileSaverModuleImpl(coreModule)
      val passwordsModule = PasswordsModuleImpl(coreModule, cryptographyModule, fileSaverModule)
      val authModule = AuthModuleImpl(coreModule)
      return CoreComponentImpl(coreModule, cryptographyModule, fileSaverModule,
        passwordsModule, authModule)
    }
  }
}

class CoreComponentImpl(
  private val coreModule: CoreModule,
  private val cryptographyModule: CryptographyModule,
  private val fileSaverModule: FileSaverModule,
  private val passwordsModule: PasswordsModule,
  private val authModule: AuthModule,
) : CoreComponent,
  CoreModule by coreModule,
  CryptographyModule by cryptographyModule,
  FileSaverModule by fileSaverModule,
  PasswordsModule by passwordsModule,
  AuthModule by authModule