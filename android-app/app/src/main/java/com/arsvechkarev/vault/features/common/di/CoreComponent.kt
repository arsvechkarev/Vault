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
import com.arsvechkarev.vault.features.common.di.modules.SettingsModule
import com.arsvechkarev.vault.features.common.di.modules.SettingsModuleImpl
import com.arsvechkarev.vault.features.common.navigation.ActivityResultWrapper

interface CoreComponent :
  CoreModule,
  KeePassModule,
  IoModule,
  PasswordsModule,
  NavigationModule,
  NotificationsModule,
  SettingsModule {
  
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
      val settingsModule = SettingsModuleImpl(coreModule)
      return CoreComponentImpl(
        coreModule = coreModule,
        keePassModule = keePassModule,
        ioModule = ioModule,
        passwordsModule = passwordsModule,
        navigationModule = NavigationModuleImpl(activityResultWrapper),
        notificationsModule = NotificationsModuleImpl(),
        settingsModule = settingsModule
      )
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
  private val settingsModule: SettingsModule,
) : CoreComponent,
  CoreModule by coreModule,
  KeePassModule by keePassModule,
  IoModule by ioModule,
  PasswordsModule by passwordsModule,
  NavigationModule by navigationModule,
  NotificationsModule by notificationsModule,
  SettingsModule by settingsModule
