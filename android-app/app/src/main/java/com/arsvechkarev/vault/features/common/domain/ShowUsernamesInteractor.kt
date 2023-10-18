package com.arsvechkarev.vault.features.common.domain

import com.arsvechkarev.vault.features.common.data.preferences.Preferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ShowUsernamesInteractor(private val preferences: Preferences) {
  
  private val _showUsernamesFlow = MutableSharedFlow<Boolean>()
  val showUsernamesFlow: SharedFlow<Boolean> get() = _showUsernamesFlow
  
  suspend fun getShowUsernames(): Boolean {
    return preferences.getBoolean(KEY)
  }
  
  suspend fun setShowUsernames(value: Boolean) {
    preferences.putBoolean(KEY, value)
    _showUsernamesFlow.emit(value)
  }
  
  companion object {
    
    const val KEY = "show_passwords_usernames"
  }
}