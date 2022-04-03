package com.arsvechkarev.vault.features.password_checking

import dagger.Module
import dagger.Subcomponent

@Module(subcomponents = [PasswordCheckingComponent::class])
object PasswordCheckingModule

@Subcomponent
interface PasswordCheckingComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(): PasswordCheckingComponent
    }

    fun providePresenter(): PasswordCheckingPresenter
}