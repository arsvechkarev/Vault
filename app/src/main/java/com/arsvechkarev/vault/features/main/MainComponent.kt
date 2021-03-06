package com.arsvechkarev.vault.features.main

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.arsvechkarev.vault.core.navigation.Navigator
import com.arsvechkarev.vault.core.navigation.NavigatorImpl
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.core.navigation.ScreenHandler
import com.arsvechkarev.vault.core.navigation.ScreenHandlerFactory
import com.arsvechkarev.vault.core.navigation.ViewNavigationHost
import com.arsvechkarev.vault.core.navigation.ViewScreen
import com.arsvechkarev.vault.core.navigation.ViewScreenHandler
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module(subcomponents = [MainComponent::class])
object MainModule

@Subcomponent(modules = [NavigationModule::class])
interface MainComponent {
  
  fun inject(activity: MainActivity)
  
  @Subcomponent.Builder
  interface Builder {
    
    @BindsInstance
    fun navigationRoot(rootView: FrameLayout): Builder
    
    fun build(): MainComponent
  }
}

@Module
class NavigationModule {
  
  private var _context: Context? = null
  
  private val factory = object : ScreenHandlerFactory {
    override fun createScreenHandler(screen: Screen): ScreenHandler {
      return ViewScreenHandler(screen as ViewScreen, _context!!)
    }
  }
  
  private val converter: (ScreenHandler) -> View = { handler ->
    (handler as ViewScreenHandler).view
  }
  
  @Provides
  fun provideNavigator(rootView: FrameLayout): Navigator {
    _context = rootView.context
    val host = ViewNavigationHost(rootView, converter)
    return NavigatorImpl(host, factory)
  }
}