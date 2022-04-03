package com.arsvechkarev.vault.core.di.modules

import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ActivityModule(private val activity: FragmentActivity) {

    @Provides
    @Singleton
    fun provideActivity(): FragmentActivity = activity
}