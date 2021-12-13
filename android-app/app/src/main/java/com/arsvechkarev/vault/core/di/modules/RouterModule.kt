package com.arsvechkarev.vault.core.di.modules

import com.github.terrakok.cicerone.Cicerone
import dagger.Module
import dagger.Provides
import navigation.Router
import javax.inject.Singleton

@Module
object RouterModule {
  
  @Provides
  @Singleton
  @JvmStatic
  fun provideRouter(): Router = Router()
  
  @Provides
  @Singleton
  @JvmStatic
  fun provideCicerone(router: Router): Cicerone<Router> = Cicerone.create(router)
}