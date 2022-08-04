package com.arsvechkarev.vault.core

import com.arsvechkarev.vault.core.model.PasswordInfoItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ListenableCachedPasswordStorage(private val cachedPasswordStorage: CachedPasswordsStorage) {
  
  private val _passwords = MutableSharedFlow<List<PasswordInfoItem>>()
  val passwords: SharedFlow<List<PasswordInfoItem>> get() = _passwords
  
  suspend fun getPasswords(masterPassword: String): List<PasswordInfoItem> {
    return cachedPasswordStorage.getPasswords(masterPassword)
  }
  
  suspend fun savePassword(masterPassword: String, passwordInfoItem: PasswordInfoItem) {
    cachedPasswordStorage.savePassword(masterPassword, passwordInfoItem)
    notifySubscribers(masterPassword)
  }
  
  suspend fun updatePassword(masterPassword: String, passwordInfoItem: PasswordInfoItem) {
    cachedPasswordStorage.updatePassword(masterPassword, passwordInfoItem)
    notifySubscribers(masterPassword)
  }
  
  suspend fun deletePassword(masterPassword: String, passwordInfoItem: PasswordInfoItem) {
    cachedPasswordStorage.deletePassword(masterPassword, passwordInfoItem)
    notifySubscribers(masterPassword)
  }
  
  private suspend fun notifySubscribers(masterPassword: String) {
    _passwords.emit(cachedPasswordStorage.getPasswords(masterPassword))
  }
}