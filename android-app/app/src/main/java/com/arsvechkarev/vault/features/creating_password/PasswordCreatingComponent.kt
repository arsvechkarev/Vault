package com.arsvechkarev.vault.features.creating_password

import com.arsvechkarev.vault.core.di.FeatureScope
import dagger.Module
import dagger.Subcomponent

@Module(subcomponents = [PasswordCreatingComponent::class])
object PasswordCreatingModule

@FeatureScope
@Subcomponent
interface PasswordCreatingComponent {
  
  fun getPresenter(): PasswordCreatingPresenter
  
  @Subcomponent.Factory
  interface Factory {
    fun create(): PasswordCreatingComponent
  }
}