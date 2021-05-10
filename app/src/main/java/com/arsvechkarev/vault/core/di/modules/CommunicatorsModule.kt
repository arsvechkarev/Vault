package com.arsvechkarev.vault.core.di.modules

import com.arsvechkarev.vault.core.communicators.BehaviorCommunicator
import com.arsvechkarev.vault.core.communicators.Communicator
import com.arsvechkarev.vault.core.communicators.PublishCommunicator
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingEvents
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingTag
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingEvents
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingTag
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
  
  @Provides
  @Singleton
  @JvmStatic
  @Named(PasswordCheckingTag)
  fun providePasswordCheckingCommunicator(): Communicator<PasswordCheckingEvents> {
    return PublishCommunicator()
  }
}