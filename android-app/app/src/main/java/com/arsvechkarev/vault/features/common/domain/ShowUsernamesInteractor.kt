package com.arsvechkarev.vault.features.common.domain

import com.arsvechkarev.vault.features.common.data.Settings

class ShowUsernamesInteractor(private val settings: Settings) {
  
  suspend fun showUsernames(): Boolean {
    return settings.getBoolean(KEY)
  }
  
  suspend fun setShowUsernames(value: Boolean) {
    settings.setBoolean(KEY, value)
  }
  
  companion object {
    
    const val KEY = "show_passwords_usernames"
  }
}