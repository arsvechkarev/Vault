package com.arsvechkarev.vault.features.initial_screen

import dagger.Module
import dagger.Subcomponent

@Module(subcomponents = [InitialComponent::class])
object InitialModule

@Subcomponent
interface InitialComponent {
  
  @Subcomponent.Factory
  interface Factory {
    
    fun create(): InitialComponent
  }
  
  fun inject(initialScreen: InitialScreen)
}