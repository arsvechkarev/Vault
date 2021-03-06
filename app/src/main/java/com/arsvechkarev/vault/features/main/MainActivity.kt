package com.arsvechkarev.vault.features.main

import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import com.arsvechkarev.vault.core.BaseActivity
import com.arsvechkarev.vault.core.UserAuthSaver
import com.arsvechkarev.vault.core.di.CoreComponent
import com.arsvechkarev.vault.core.model.Service
import com.arsvechkarev.vault.core.navigation.AppNavigator
import com.arsvechkarev.vault.core.navigation.ForwardOptions
import com.arsvechkarev.vault.core.navigation.Navigator
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreen
import com.arsvechkarev.vault.features.creating_service.CreatingServiceScreen
import com.arsvechkarev.vault.features.info.InfoScreen
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.SERVICE_INFO
import com.arsvechkarev.vault.features.initial_screen.InitialScreen
import com.arsvechkarev.vault.features.services_list.ServicesListScreen
import com.arsvechkarev.vault.features.start.StartScreen
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewdsl.Densities
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.classNameTag
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import com.arsvechkarev.vault.views.NavigatorRootView
import javax.inject.Inject

class MainActivity : BaseActivity(), AppNavigator {
  
  private val mainActivityLayout
    get() = withViewBuilder {
      RootFrameLayout {
        child<NavigatorRootView>(MatchParent, MatchParent) {
          classNameTag()
        }
      }
    }
  
  @Inject
  lateinit var navigator: Navigator
  
  @Inject
  lateinit var userAuthSaver: UserAuthSaver
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Densities.init(resources)
    Colors.init(this)
    window.decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE
        or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    setContentView(mainActivityLayout)
    CoreComponent.instance.getMainComponentBuilder()
        .navigationRoot(viewAs<NavigatorRootView>())
        .build()
        .inject(this)
    if (userAuthSaver.isUserAuthorized()) {
      navigator.goTo(StartScreen::class)
    } else {
      navigator.goTo(InitialScreen::class)
    }
  }
  
  override fun onBackPressed() {
    if (!navigator.handleGoBack()) {
      super.onBackPressed()
    }
  }
  
  override fun goToCreatingMasterPasswordScreen() {
    navigator.goTo(CreatingMasterPasswordScreen::class)
  }
  
  override fun goToServicesListScreen() {
    navigator.switchToNewRoot(ServicesListScreen::class)
  }
  
  override fun goToNewServiceScreen() {
    navigator.goTo(CreatingServiceScreen::class)
  }
  
  override fun goToInfoScreen(service: Service) {
    navigator.goTo(InfoScreen::class, options = ForwardOptions(
      arguments = mapOf(SERVICE_INFO to service)
    ))
  }
  
  override fun popCurrentScreen() {
    navigator.goBack()
  }
}