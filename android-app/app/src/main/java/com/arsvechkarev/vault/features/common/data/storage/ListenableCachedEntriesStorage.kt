package com.arsvechkarev.vault.features.common.data.storage

import buisnesslogic.model.Entries
import buisnesslogic.model.Entry
import com.arsvechkarev.vault.features.common.model.CreditCardItem
import com.arsvechkarev.vault.features.common.model.PasswordItem
import com.arsvechkarev.vault.features.common.model.PlainTextItem
import com.arsvechkarev.vault.features.common.model.toCreditCardItem
import com.arsvechkarev.vault.features.common.model.toPasswordItem
import com.arsvechkarev.vault.features.common.model.toPlainTextItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ListenableCachedEntriesStorage(private val cachedPasswordStorage: CachedEntriesStorage) {
  
  private val _entries = MutableSharedFlow<Entries>()
  val entries: SharedFlow<Entries> get() = _entries
  
  suspend fun getEntries(masterPassword: String): Entries {
    return cachedPasswordStorage.getEntries(masterPassword)
  }
  
  suspend fun saveEntries(masterPassword: String, entries: Entries) {
    cachedPasswordStorage.saveEntries(masterPassword, entries)
    notifySubscribers(masterPassword)
  }
  
  suspend fun savePassword(
    masterPassword: String,
    websiteName: String,
    login: String,
    password: String
  ): PasswordItem {
    val item = cachedPasswordStorage.savePassword(
      masterPassword = masterPassword,
      websiteName = websiteName,
      login = login,
      password = password,
      notes = ""
    )
    notifySubscribers(masterPassword)
    return item.toPasswordItem()
  }
  
  suspend fun saveCreditCard(
    masterPassword: String,
    cardNumber: String,
    expirationDate: String,
    cardholderName: String,
    cvcCode: String,
    pinCode: String,
    notes: String,
  ): CreditCardItem {
    val item = cachedPasswordStorage.saveCreditCard(masterPassword, cardNumber, expirationDate,
      cardholderName, cvcCode, pinCode, notes)
    notifySubscribers(masterPassword)
    return item.toCreditCardItem()
  }
  
  suspend fun savePlainText(masterPassword: String, title: String, text: String): PlainTextItem {
    val item = cachedPasswordStorage.savePlainText(masterPassword, title, text)
    notifySubscribers(masterPassword)
    return item.toPlainTextItem()
  }
  
  suspend fun updateEntry(masterPassword: String, entry: Entry) {
    cachedPasswordStorage.updateEntry(masterPassword, entry)
    notifySubscribers(masterPassword)
  }
  
  suspend fun deleteEntry(masterPassword: String, entry: Entry) {
    cachedPasswordStorage.deleteEntry(masterPassword, entry)
    notifySubscribers(masterPassword)
  }
  
  suspend fun notifySubscribers(masterPassword: String, reloadData: Boolean = false) {
    val value = cachedPasswordStorage.getEntries(masterPassword, reloadData)
    _entries.emit(value)
  }
}
