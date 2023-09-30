package com.arsvechkarev.vault.features.common.di.modules

import android.content.Context
import com.arsvechkarev.vault.features.common.data.AndroidSharedPreferences
import com.arsvechkarev.vault.features.common.data.Preferences
import com.arsvechkarev.vault.features.common.domain.ShowUsernamesInteractor

interface PreferencesModule {
  val settingsPreferences: Preferences
  val imagesNamesPreferences: Preferences
  val showUsernamesInteractor: ShowUsernamesInteractor
}

class PreferencesModuleImpl(
  coreModule: CoreModule
) : PreferencesModule {
  
  override val settingsPreferences = AndroidSharedPreferences(
    coreModule.application.getSharedPreferences(SETTINGS_FILENAME, Context.MODE_PRIVATE)
  )
  
  override val imagesNamesPreferences = AndroidSharedPreferences(
    coreModule.application.getSharedPreferences(IMAGES_NAMES_FILENAME, Context.MODE_PRIVATE)
  )
  
  override val showUsernamesInteractor = ShowUsernamesInteractor(settingsPreferences)
  
  companion object {
    
    const val SETTINGS_FILENAME = "settings"
    const val IMAGES_NAMES_FILENAME = "images_names"
  }
}
