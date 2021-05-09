package com.arsvechkarev.vault.features.settings.di

import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.features.settings.presentation.SettingsPresenter
import dagger.Module
import dagger.Subcomponent

@Module(subcomponents = [SettingsComponent::class])
object SettingsModule

@FeatureScope
@Subcomponent
interface SettingsComponent {
  
  @Subcomponent.Factory
  interface Factory {
    
    fun create(): SettingsComponent
  }
  
  fun providePresenter(): SettingsPresenter
}