package com.arsvechkarev.vault.features.common.di.modules

import android.content.Context
import com.arsvechkarev.vault.features.common.data.preferences.AndroidSharedPreferences
import com.arsvechkarev.vault.features.common.data.preferences.Preferences

interface PreferencesModule {
  val settingsPreferences: Preferences
  val imagesNamesPreferences: Preferences
  val biometricsDataPreferences: Preferences
  val biometricsMetadataPreferences: Preferences
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
  
  override val biometricsDataPreferences = AndroidSharedPreferences(
    coreModule.application.getSharedPreferences(BIOMETRICS_DATA_FILENAME, Context.MODE_PRIVATE)
  )
  
  override val biometricsMetadataPreferences = AndroidSharedPreferences(
    coreModule.application.getSharedPreferences(BIOMETRICS_METADATA_FILENAME, Context.MODE_PRIVATE)
  )
  
  companion object {
    
    const val SETTINGS_FILENAME = "settings"
    const val IMAGES_NAMES_FILENAME = "images_names"
    const val BIOMETRICS_DATA_FILENAME = "biometrics_data"
    const val BIOMETRICS_METADATA_FILENAME = "biometrics_metadata"
  }
}
