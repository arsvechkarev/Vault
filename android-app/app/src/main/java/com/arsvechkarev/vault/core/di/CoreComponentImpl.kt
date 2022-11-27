package com.arsvechkarev.vault.core.di

import android.app.Application
import com.arsvechkarev.vault.core.di.modules.AuthModule
import com.arsvechkarev.vault.core.di.modules.AuthModuleImpl
import com.arsvechkarev.vault.core.di.modules.CoreModule
import com.arsvechkarev.vault.core.di.modules.CoreModuleImpl
import com.arsvechkarev.vault.core.di.modules.CryptographyModule
import com.arsvechkarev.vault.core.di.modules.CryptographyModuleImpl
import com.arsvechkarev.vault.core.di.modules.IoModule
import com.arsvechkarev.vault.core.di.modules.IoModuleImpl
import com.arsvechkarev.vault.core.di.modules.PasswordsModule
import com.arsvechkarev.vault.core.di.modules.PasswordsModuleImpl

interface CoreComponent :
  CoreModule,
  CryptographyModule,
  IoModule,
  PasswordsModule,
  AuthModule {
  
  companion object {
    
    fun create(application: Application): CoreComponent {
      val coreModule = CoreModuleImpl(application)
      val cryptographyModule = CryptographyModuleImpl()
      val fileSaverModule = IoModuleImpl(coreModule)
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
  private val ioModule: IoModule,
  private val passwordsModule: PasswordsModule,
  private val authModule: AuthModule,
) : CoreComponent,
  CoreModule by coreModule,
  CryptographyModule by cryptographyModule,
  IoModule by ioModule,
  PasswordsModule by passwordsModule,
  AuthModule by authModule
