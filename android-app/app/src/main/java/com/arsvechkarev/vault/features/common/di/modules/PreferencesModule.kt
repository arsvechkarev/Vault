package com.arsvechkarev.vault.features.common.di.modules

import android.content.Context
import com.arsvechkarev.vault.features.common.data.AndroidSharedPreferences
import com.arsvechkarev.vault.features.common.data.Preferences
import com.arsvechkarev.vault.features.common.domain.ShowUsernamesInteractor

interface PreferencesModule {
  val preferences: Preferences
  val showUsernamesInteractor: ShowUsernamesInteractor
}

class PreferencesModuleImpl(
  coreModule: CoreModule
) : PreferencesModule {
  
  override val preferences = AndroidSharedPreferences(
    coreModule.application.getSharedPreferences(SETTINGS_FILENAME, Context.MODE_PRIVATE)
  )
  
  override val showUsernamesInteractor = ShowUsernamesInteractor(preferences)
  
  companion object {
    
    const val SETTINGS_FILENAME = "settings"
  }
}
