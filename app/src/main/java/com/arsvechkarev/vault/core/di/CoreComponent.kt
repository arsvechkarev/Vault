package com.arsvechkarev.vault.core.di

import android.content.Context
import com.arsvechkarev.vault.core.di.modules.CoreModule
import com.arsvechkarev.vault.core.di.modules.CryptographyModule
import com.arsvechkarev.vault.core.di.modules.FileSaverModule
import com.arsvechkarev.vault.core.di.modules.RouterModule
import com.arsvechkarev.vault.core.di.modules.ServicesModule
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordComponent
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordModule
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingComponent
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingModule
import com.arsvechkarev.vault.features.creating_service.CreatingServiceComponent
import com.arsvechkarev.vault.features.creating_service.CreatingServiceModule
import com.arsvechkarev.vault.features.info.InfoComponent
import com.arsvechkarev.vault.features.info.InfoModule
import com.arsvechkarev.vault.features.initial_screen.InitialComponent
import com.arsvechkarev.vault.features.main.MainComponent
import com.arsvechkarev.vault.features.main.NavigationModule
import com.arsvechkarev.vault.features.services_list.ServicesListComponent
import com.arsvechkarev.vault.features.services_list.ServicesListModule
import com.arsvechkarev.vault.features.start.StartComponent
import com.arsvechkarev.vault.features.start.StartModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    CoreModule::class,
    RouterModule::class,
    CryptographyModule::class,
    FileSaverModule::class,
    ServicesModule::class,
    CreatingMasterPasswordModule::class,
    CreatingServiceModule::class,
    InfoModule::class,
    ServicesListModule::class,
    StartModule::class,
    PasswordCreatingModule::class,
    NavigationModule::class
  ]
)
interface CoreComponent {
  
  fun getMainComponentBuilder(): MainComponent.Builder
  
  fun getInitialComponentFactory(): InitialComponent.Factory
  
  fun getCreatingMasterPasswordComponentFactory(): CreatingMasterPasswordComponent.Factory
  
  fun getPasswordCreatingComponentFactory(): PasswordCreatingComponent.Factory
  
  fun getCreatingServiceComponentFactory(): CreatingServiceComponent.Factory
  
  fun getInfoComponentFactory(): InfoComponent.Factory
  
  fun getServicesListComponentFactory(): ServicesListComponent.Factory
  
  fun getStartComponentFactory(): StartComponent.Factory
  
  @Component.Builder
  interface Builder {
    
    fun coreModule(coreModule: CoreModule): Builder
    
    fun build(): CoreComponent
  }
  
  companion object {
    
    private var _instance: CoreComponent? = null
    val instance: CoreComponent get() = _instance!!
    
    fun init(applicationContext: Context) {
      _instance = DaggerCoreComponent.builder()
          .coreModule(CoreModule(applicationContext))
          .build()
    }
  }
}