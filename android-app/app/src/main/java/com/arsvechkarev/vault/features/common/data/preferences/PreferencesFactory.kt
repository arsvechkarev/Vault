package com.arsvechkarev.vault.features.common.data.preferences

import android.content.Context

interface PreferencesFactory {
  
  fun providePreferences(name: String): Preferences
}

class PreferencesFactoryImpl(
  private val context: Context
) : PreferencesFactory {
  
  override fun providePreferences(name: String): Preferences {
    return AndroidSharedPreferences(context.getSharedPreferences(name, Context.MODE_PRIVATE))
  }
}
