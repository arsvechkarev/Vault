package com.arsvechkarev.vault.features.start

import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.core.di.PasswordActionsModule
import dagger.Module
import dagger.Subcomponent

@Module(
  includes = [PasswordActionsModule::class],
  subcomponents = [StartComponent::class]
)
object StartModule

@FeatureScope
@Subcomponent
interface StartComponent {
  
  @Subcomponent.Factory
  interface Factory {
    
    fun create(): StartComponent
  }
  
  fun providePresenter(): StartPresenter
}