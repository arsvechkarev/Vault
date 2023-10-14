package com.arsvechkarev.vault.features.common.di

import android.app.Application
import com.arsvechkarev.vault.features.common.data.files.ExternalFileReader
import com.arsvechkarev.vault.features.common.data.files.PasswordsFileExporter
import com.arsvechkarev.vault.features.common.di.modules.CoreModule
import com.arsvechkarev.vault.features.common.di.modules.CoreModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.ImagesLoadingModule
import com.arsvechkarev.vault.features.common.di.modules.ImagesLoadingModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.IoModule
import com.arsvechkarev.vault.features.common.di.modules.IoModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.KeePassModule
import com.arsvechkarev.vault.features.common.di.modules.KeePassModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.NavigationModule
import com.arsvechkarev.vault.features.common.di.modules.NavigationModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.ObserversModule
import com.arsvechkarev.vault.features.common.di.modules.ObserversModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.PasswordsModule
import com.arsvechkarev.vault.features.common.di.modules.PasswordsModuleImpl
import com.arsvechkarev.vault.features.common.di.modules.PreferencesModule
import com.arsvechkarev.vault.features.common.di.modules.PreferencesModuleImpl
import com.arsvechkarev.vault.features.common.navigation.ActivityResultWrapper

interface CoreComponent :
  CoreModule,
  KeePassModule,
  IoModule,
  PasswordsModule,
  NavigationModule,
  ObserversModule,
  PreferencesModule,
  ImagesLoadingModule {
  
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
      val preferencesModule = PreferencesModuleImpl(coreModule)
      return CoreComponentImpl(
        coreModule = coreModule,
        keePassModule = keePassModule,
        ioModule = ioModule,
        passwordsModule = PasswordsModuleImpl(keePassModule),
        navigationModule = NavigationModuleImpl(activityResultWrapper),
        observersModule = ObserversModuleImpl(),
        preferencesModule = preferencesModule,
        imagesLoadingModule = ImagesLoadingModuleImpl(coreModule, ioModule, preferencesModule),
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
  private val observersModule: ObserversModule,
  private val imagesLoadingModule: ImagesLoadingModule,
  private val preferencesModule: PreferencesModule,
) : CoreComponent,
  CoreModule by coreModule,
  KeePassModule by keePassModule,
  IoModule by ioModule,
  PasswordsModule by passwordsModule,
  NavigationModule by navigationModule,
  ObserversModule by observersModule,
  PreferencesModule by preferencesModule,
  ImagesLoadingModule by imagesLoadingModule
