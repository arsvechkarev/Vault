package com.arsvechkarev.vault.features.creating_master_password

import android.content.Context
import com.arsvechkarev.vault.core.UserAuthSaver
import com.arsvechkarev.vault.core.UserAuthSaverImpl
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.core.di.PasswordActionsModule
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Singleton

@Module(
  includes = [PasswordActionsModule::class],
  subcomponents = [CreatingMasterPasswordComponent::class]
)
object CreatingMasterPasswordModule {
  
  @Provides
  @JvmStatic
  @Singleton
  fun provideUserAuthSaver(context: Context): UserAuthSaver {
    return UserAuthSaverImpl(context)
  }
}

@FeatureScope
@Subcomponent
interface CreatingMasterPasswordComponent {
  
  @Subcomponent.Factory
  interface Factory {
    fun create(): CreatingMasterPasswordComponent
  }
  
  fun providePresenter(): CreatingMasterPasswordPresenter
}