package com.arsvechkarev.vault.features.services_list.di

import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.features.services_list.presentation.ServicesListPresenter
import dagger.Module
import dagger.Subcomponent

@Module(subcomponents = [ServicesListComponent::class])
object ServicesListModule

@FeatureScope
@Subcomponent
interface ServicesListComponent {
  
  @Subcomponent.Factory
  interface Factory {
    
    fun create(): ServicesListComponent
  }
  
  fun providePresenter(): ServicesListPresenter
}