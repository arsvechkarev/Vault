package com.arsvechkarev.vault.features.common.data.storage

import com.arsvechkarev.vault.core.model.PasswordInfoItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ListenableCachedPasswordsStorage(private val cachedPasswordStorage: CachedPasswordsStorage) {
  
  private val _passwords = MutableSharedFlow<List<PasswordInfoItem>>()
  val passwords: SharedFlow<List<PasswordInfoItem>> get() = _passwords
  
  suspend fun getPasswords(masterPassword: String): List<PasswordInfoItem> {
    return cachedPasswordStorage.getPasswords(masterPassword)
  }
  
  suspend fun savePasswords(masterPassword: String, passwords: List<PasswordInfoItem>) {
    cachedPasswordStorage.savePasswords(masterPassword, passwords)
    notifySubscribers(masterPassword)
  }
  
  suspend fun savePassword(
    masterPassword: String,
    websiteName: String,
    login: String,
    password: String
  ): PasswordInfoItem {
    val item = cachedPasswordStorage.savePassword(masterPassword, websiteName, login, password)
    notifySubscribers(masterPassword)
    return item
  }
  
  suspend fun updatePassword(masterPassword: String, passwordInfoItem: PasswordInfoItem) {
    cachedPasswordStorage.updatePassword(masterPassword, passwordInfoItem)
    notifySubscribers(masterPassword)
  }
  
  suspend fun deletePassword(masterPassword: String, passwordInfoItem: PasswordInfoItem) {
    cachedPasswordStorage.deletePassword(masterPassword, passwordInfoItem)
    notifySubscribers(masterPassword)
  }
  
  suspend fun notifySubscribers(masterPassword: String) {
    val value = cachedPasswordStorage.getPasswords(masterPassword)
    _passwords.emit(value)
  }
}
