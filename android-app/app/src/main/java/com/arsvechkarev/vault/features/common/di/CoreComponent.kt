package com.arsvechkarev.vault.features.common.di

import android.app.Application
import com.arsvechkarev.vault.features.common.data.ExternalFileReader
import com.arsvechkarev.vault.features.common.data.PasswordsFileExporter
import com.arsvechkarev.vault.features.common.di.modules.CoreModule
import com.arsvechkarev.vault.features.common.di.modules.CoreModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.IoModule
import com.arsvechkarev.vault.features.common.di.modules.IoModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.KeePassModule
import com.arsvechkarev.vault.features.common.di.modules.KeePassModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.NavigationModule
import com.arsvechkarev.vault.features.common.di.modules.NavigationModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.NotificationsModule
import com.arsvechkarev.vault.features.common.di.modules.NotificationsModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.PasswordsModule
import com.arsvechkarev.vault.features.common.di.modules.PasswordsModuleImpl
import com.arsvechkarev.vault.features.common.navigation.ActivityResultWrapper

interface CoreComponent :
  CoreModule,
  KeePassModule,
  IoModule,
  PasswordsModule,
  NavigationModule,
  NotificationsModule {
  
  companion object {
    
    fun create(
      application: Application,
      activityResultWrapper: ActivityResultWrapper,
      passwordsFileExporter: PasswordsFileExporter,
      externalFileReader: ExternalFileReader,
    ): CoreComponent {
      val coreModule = CoreModuleImpl(application)
      val keePassModule = KeePassModuleImpl(coreModule)
      val ioModule = IoModuleImpl(coreModule, externalFileReader, passwordsFileExporter)
      val passwordsModule = PasswordsModuleImpl(keePassModule)
      return CoreComponentImpl(coreModule, keePassModule, ioModule,
        passwordsModule, NavigationModuleImpl(activityResultWrapper), NotificationsModuleImpl())
    }
  }
}

class CoreComponentImpl(
  private val coreModule: CoreModule,
  private val keePassModule: KeePassModule,
  private val ioModule: IoModule,
  private val passwordsModule: PasswordsModule,
  private val navigationModule: NavigationModule,
  private val notificationsModule: NotificationsModule,
) : CoreComponent,
  CoreModule by coreModule,
  KeePassModule by keePassModule,
  IoModule by ioModule,
  PasswordsModule by passwordsModule,
  NavigationModule by navigationModule,
  NotificationsModule by notificationsModule
