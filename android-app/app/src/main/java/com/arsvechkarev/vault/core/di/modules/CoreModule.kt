package com.arsvechkarev.vault.core.di.modules

import android.content.Context
import com.arsvechkarev.vault.core.DefaultDispatchers
import com.arsvechkarev.vault.core.Dispatchers
import com.arsvechkarev.vault.core.di.ApplicationScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
class CoreModule(private val context: Context) {

    @Provides
    fun provideDispatchers(): Dispatchers = DefaultDispatchers

    @Provides
    fun provideContext(): Context = context

    @Provides
    @Singleton
    @ApplicationScope
    fun provideScope(dispatchers: Dispatchers): CoroutineScope = CoroutineScope(dispatchers.Main)
}