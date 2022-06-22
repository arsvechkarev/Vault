package com.arsvechkarev.vault.features.info

import android.content.Context
import com.arsvechkarev.vault.common.Clipboard
import com.arsvechkarev.vault.common.ClipboardImpl
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.core.di.modules.CoreModule
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
  fun provideClipboard(context: Context): Clipboard = ClipboardImpl(context)
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
