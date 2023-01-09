package com.arsvechkarev.vault.features.common

import com.arsvechkarev.vault.core.extensions.bundleOf
import com.arsvechkarev.vault.core.model.PasswordItem
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordScreen
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreen
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordScreen
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryScreen
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsScreen
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen
import com.arsvechkarev.vault.features.initial.InitialScreen
import com.arsvechkarev.vault.features.login.LoginScreen
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen
import com.arsvechkarev.vault.features.settings.SettingsScreen
import navigation.Screen
import navigation.ScreenInfo

object Screens {
  
  val InitialScreen = Screen { InitialScreen::class }
  
  val CreatingMasterPasswordScreen = Screen { CreatingMasterPasswordScreen::class }
  
  val LoginScreen = Screen { LoginScreen::class }
  
  val MainListScreen = Screen { MainListScreen::class }
  
  val CreatingPasswordEntryScreen = Screen { CreatingPasswordEntryScreen::class }
  
  fun PasswordInfoScreen(passwordItem: PasswordItem): ScreenInfo {
    val pair = PasswordItem::class.qualifiedName!! to passwordItem
    return Screen(arguments = bundleOf(pair)) { PasswordInfoScreen::class }
  }
  
  val CreatingPasswordScreen = Screen { CreatingPasswordScreen::class }
  
  val SettingsScreen = Screen { SettingsScreen::class }
  
  val ChangeMasterPasswordScreen = Screen { ChangeMasterPasswordScreen::class }
  
  val ExportPasswordsScreen = Screen { ExportPasswordsScreen::class }
  
  val ImportPasswordsScreen = Screen { ImportPasswordsScreen::class }
}
