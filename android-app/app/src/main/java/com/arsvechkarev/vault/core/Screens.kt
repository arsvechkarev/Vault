package com.arsvechkarev.vault.core

import com.arsvechkarev.vault.core.extensions.bundle
import com.arsvechkarev.vault.core.model.PasswordInfoItem
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryScreen
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreen
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordScreen
import com.arsvechkarev.vault.features.info.InfoScreen
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.SERVICE
import com.arsvechkarev.vault.features.initial.InitialScreen
import com.arsvechkarev.vault.features.login.LoginScreen
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.features.settings.SettingsScreen
import navigation.Screen

object Screens {
  
  val InitialScreen = Screen { InitialScreen::class }
  
  val CreateMasterPasswordScreen = Screen { CreatingMasterPasswordScreen::class }
  
  val LoginScreen = Screen { LoginScreen::class }
  
  val MainListScreen = Screen { MainListScreen::class }
  
  val CreatingEntryScreen = Screen { CreatingEntryScreen::class }
  
  fun InfoScreen(passwordInfoItem: PasswordInfoItem) =
      Screen(arguments = bundle(SERVICE to passwordInfoItem)) { InfoScreen::class }
  
  val CreatingPasswordScreen = Screen { CreatingPasswordScreen::class }
  
  val SettingsScreen = Screen { SettingsScreen::class }
}
