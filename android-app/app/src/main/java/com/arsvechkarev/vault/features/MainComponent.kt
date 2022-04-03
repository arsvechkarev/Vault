package com.arsvechkarev.vault.features

import android.widget.FrameLayout
import com.arsvechkarev.vault.core.di.FeatureScope
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import moxy.MvpAppCompatActivity
import navigation.*

@Module(subcomponents = [MainComponent::class])
object MainModule

@FeatureScope
@Subcomponent(modules = [NavigationModule::class])
interface MainComponent {

    fun inject(activity: MainActivity)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun activity(activity: MvpAppCompatActivity): Builder

        @BindsInstance
        fun rootViewId(rootViewId: Int): Builder

        fun build(): MainComponent
    }
}

@Module
object NavigationModule {

    @Provides
    @JvmStatic
    fun provideNavigator(activity: MvpAppCompatActivity, rootViewId: Int): ExtendedNavigator {
        val rootView = activity.findViewById<FrameLayout>(rootViewId)
        val screenHandlerViewProvider =
            { handler: ScreenHandler -> (handler as MvpViewScreenHandler).view }
        val navHost = ViewNavigationHost(rootView, screenHandlerViewProvider)
        val screenHandlerFactory = ScreenHandlerFactory { screenKey, screen ->
            MvpViewScreenHandler(
                screen as BaseScreen, screenKey.toString(),
                activity.mvpDelegate, activity
            )
        }
        return ExtendedNavigatorImpl(navHost, OfClassNameFactory, screenHandlerFactory)
    }
}