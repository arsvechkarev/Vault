package com.arsvechkarev.vault.features.common.di.modules

import android.content.Context
import com.arsvechkarev.vault.features.common.data.Settings
import com.arsvechkarev.vault.features.common.data.SharedPreferencesSettings
import com.arsvechkarev.vault.features.common.domain.ShowUsernamesInteractor

interface SettingsModule {
  val settings: Settings
  val showUsernamesInteractor: ShowUsernamesInteractor
}

class SettingsModuleImpl(
  coreModule: CoreModule
) : SettingsModule {
  
  override val settings = SharedPreferencesSettings(
    coreModule.application.getSharedPreferences(SETTINGS_FILENAME, Context.MODE_PRIVATE)
  )
  
  override val showUsernamesInteractor = ShowUsernamesInteractor(settings)
  
  companion object {
    
    const val SETTINGS_FILENAME = "settings"
  }
}
