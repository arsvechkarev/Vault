package com.arsvechkarev.vault.features.creating_master_password

import android.content.Context
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.core.di.modules.PasswordActionsModule
import com.arsvechkarev.vault.features.common.UserAuthSaver
import com.arsvechkarev.vault.features.common.UserAuthSaverImpl
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