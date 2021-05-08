package com.arsvechkarev.vault.core

import com.arsvechkarev.vault.core.extensions.bundle
import com.arsvechkarev.vault.core.model.Service
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreen
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingScreen
import com.arsvechkarev.vault.features.creating_service.CreatingServiceScreen
import com.arsvechkarev.vault.features.info.InfoScreen
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.SERVICE
import com.arsvechkarev.vault.features.initial_screen.InitialScreen
import com.arsvechkarev.vault.features.services_list.ServicesListScreen
import com.arsvechkarev.vault.features.start.StartScreen
import navigation.Screen

object Screens {
  
  val InitialScreen = Screen { InitialScreen::class }
  
  val CreateMasterPasswordScreen = Screen { CreatingMasterPasswordScreen::class }
  
  val StartScreen = Screen { StartScreen::class }
  
  val ServicesListScreen = Screen { ServicesListScreen::class }
  
  val CreatingServiceScreen = Screen { CreatingServiceScreen::class }
  
  fun InfoScreen(service: Service) =
      Screen(arguments = bundle(SERVICE to service)) { InfoScreen::class }
  
  val PasswordCreatingScreen = Screen { PasswordCreatingScreen::class }
}