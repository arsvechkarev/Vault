package com.arsvechkarev.vault.core.di

import android.content.Context
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordComponent
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordModule
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingComponent
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingModule
import com.arsvechkarev.vault.features.creating_service.CreatingServiceComponent
import com.arsvechkarev.vault.features.creating_service.CreatingServiceModule
import com.arsvechkarev.vault.features.info.InfoComponent
import com.arsvechkarev.vault.features.info.InfoModule
import com.arsvechkarev.vault.features.main.MainComponent
import com.arsvechkarev.vault.features.main.MainModule
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
    CryptographyModule::class,
    FileSaverModule::class,
    ServicesModule::class,
    CreatingMasterPasswordModule::class,
    CreatingServiceModule::class,
    InfoModule::class,
    ServicesListModule::class,
    StartModule::class,
    PasswordCreatingModule::class,
    MainModule::class
  ]
)
interface CoreComponent {
  
  fun getMainComponentBuilder(): MainComponent.Builder
  
  fun getCreatingMasterPasswordComponent(): CreatingMasterPasswordComponent.Factory
  
  fun getPasswordCreatingComponent(): PasswordCreatingComponent.Factory
  
  fun getCreatingServiceComponent(): CreatingServiceComponent.Factory
  
  fun getInfoComponent(): InfoComponent.Factory
  
  fun getServicesListComponent(): ServicesListComponent.Factory
  
  fun getStartComponent(): StartComponent.Factory
  
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