package com.arsvechkarev.vault.features.creating_service

import dagger.Module
import dagger.Subcomponent

@Module(subcomponents = [CreatingServiceComponent::class])
object CreatingServiceModule

@Subcomponent
interface CreatingServiceComponent {
  
  @Subcomponent.Factory
  interface Factory {
    
    fun create(): CreatingServiceComponent
  }
  
  fun providePresenter(): CreatingServicePresenter
}