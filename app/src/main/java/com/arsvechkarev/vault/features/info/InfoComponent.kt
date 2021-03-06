package com.arsvechkarev.vault.features.info

import android.content.Context
import com.arsvechkarev.vault.core.AndroidClipboard
import com.arsvechkarev.vault.core.Clipboard
import com.arsvechkarev.vault.core.di.CoreModule
import com.arsvechkarev.vault.core.di.FeatureScope
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Singleton

@Module(
  includes = [CoreModule::class],
  subcomponents = [InfoComponent::class]
)
object InfoModule {
  
  @Provides
  @JvmStatic
  @Singleton
  fun provideClipboard(context: Context): Clipboard = AndroidClipboard(context)
}

@FeatureScope
@Subcomponent
interface InfoComponent {
  
  @Subcomponent.Factory
  interface Factory {
    
    fun create(): InfoComponent
  }
  
  fun providePresenter(): InfoPresenter
}