package com.arsvechkarev.vault.features.settings

import com.arsvechkarev.vault.core.di.FeatureScope
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