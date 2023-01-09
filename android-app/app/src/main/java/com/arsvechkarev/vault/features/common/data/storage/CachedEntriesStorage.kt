package com.arsvechkarev.vault.features.common.data.storage

import buisnesslogic.EntriesStorage
import buisnesslogic.IdGenerator
import buisnesslogic.model.CreditCard
import buisnesslogic.model.Entries
import buisnesslogic.model.Entry
import buisnesslogic.model.Password
import buisnesslogic.model.PlainText

class CachedEntriesStorage(
  private val storage: EntriesStorage,
  private val idGenerator: IdGenerator,
) {
  
  private var entries: Entries? = null
  
  suspend fun getEntries(
    masterPassword: String,
    reloadData: Boolean = false
  ): Entries {
    if (entries == null || reloadData) {
      entries = storage.getEntries(masterPassword)
    }
    return checkNotNull(entries)
  }
  
  suspend fun saveEntries(masterPassword: String, entries: Entries) {
    storage.saveEntries(masterPassword, entries)
    this.entries = entries
  }
  
  suspend fun savePassword(
    masterPassword: String,
    websiteName: String,
    login: String,
    password: String,
    notes: String
  ): Password {
    val id = getUniqueId()
    val item = Password(id, websiteName, login, password, notes)
    entries = storage.saveEntry(masterPassword, item)
    return item
  }
  
  suspend fun saveCreditCard(
    masterPassword: String,
    cardNumber: String,
    expirationDate: String,
    cardholderName: String,
    cvcCode: String,
    pinCode: String,
    notes: String,
  ): CreditCard {
    val id = getUniqueId()
    val item = CreditCard(id, cardNumber, expirationDate, cardholderName, cvcCode, pinCode, notes)
    entries = storage.saveEntry(masterPassword, item)
    return item
  }
  
  suspend fun savePlainText(masterPassword: String, title: String, text: String): PlainText {
    val id = getUniqueId()
    val item = PlainText(id, title, text)
    entries = storage.saveEntry(masterPassword, item)
    return item
  }
  
  suspend fun updateEntry(masterPassword: String, entry: Entry) {
    entries = storage.updateEntry(masterPassword, entry)
  }
  
  suspend fun deleteEntry(masterPassword: String, entry: Entry) {
    entries = storage.deleteEntry(masterPassword, entry)
  }
  
  private fun getUniqueId(): String {
    while (true) {
      val id = idGenerator.generateRandomId()
      val entries = entries ?: return id
      if (entries.passwords.any { it.id == id }
          || entries.creditCards.any { it.id == id }
          || entries.plainTexts.any { it.id == id }) {
        continue
      }
      return id
    }
  }
}
