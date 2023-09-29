package com.arsvechkarev.vault.features.common.domain

import com.arsvechkarev.vault.features.common.data.Settings
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ShowUsernamesInteractor(private val settings: Settings) {
  
  private val _showUsernamesFlow = MutableSharedFlow<Boolean>()
  val showUsernamesFlow: SharedFlow<Boolean> get() = _showUsernamesFlow
  
  suspend fun getShowUsernames(): Boolean {
    return settings.getBoolean(KEY)
  }
  
  suspend fun setShowUsernames(value: Boolean) {
    settings.setBoolean(KEY, value)
    _showUsernamesFlow.emit(value)
  }
  
  companion object {
    
    const val KEY = "show_passwords_usernames"
  }
}