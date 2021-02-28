package com.arsvechkarev.vault.features.services_list

import com.arsvechkarev.vault.core.di.FeatureScope
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