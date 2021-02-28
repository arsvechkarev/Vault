package com.arsvechkarev.vault.core.di

import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordComponent
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordModule
import com.arsvechkarev.vault.features.creating_service.CreatingServiceComponent
import com.arsvechkarev.vault.features.creating_service.CreatingServiceModule
import com.arsvechkarev.vault.features.info.InfoComponent
import com.arsvechkarev.vault.features.info.InfoModule
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
    StartModule::class
  ]
)
interface CoreComponent {
  
  fun getCreatingMasterPasswordComponent(): CreatingMasterPasswordComponent.Factory
  
  fun getCreatingServiceComponent(): CreatingServiceComponent.Factory
  
  fun getInfoComponent(): InfoComponent.Factory
  
  fun getServicesListComponent(): ServicesListComponent.Factory
  
  fun getStartComponent(): StartComponent.Factory
  
  @Component.Builder
  interface Builder {
    
    fun coreModule(coreModule: CoreModule): Builder
    
    fun build(): CoreComponent
  }
}