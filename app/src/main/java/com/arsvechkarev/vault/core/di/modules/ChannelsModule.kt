package com.arsvechkarev.vault.core.di.modules

import com.arsvechkarev.vault.core.channels.BehaviorChannel
import com.arsvechkarev.vault.core.channels.Channel
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingEvents
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingTag
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
object ChannelsModule {
  
  @Provides
  @Singleton
  @JvmStatic
  @Named(PasswordCreatingTag)
  fun provideChannel(): Channel<PasswordCreatingEvents> = BehaviorChannel()
}