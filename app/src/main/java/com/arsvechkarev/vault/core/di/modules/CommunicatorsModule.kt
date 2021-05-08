package com.arsvechkarev.vault.core.di.modules

import com.arsvechkarev.vault.core.channels.BehaviorCommunicator
import com.arsvechkarev.vault.core.channels.Communicator
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingEvents
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingTag
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
object CommunicatorsModule {
  
  @Provides
  @Singleton
  @JvmStatic
  @Named(PasswordCreatingTag)
  fun providePasswordCreatingCommunicator(): Communicator<PasswordCreatingEvents> {
    return BehaviorCommunicator()
  }
}