package buisnesslogic

import buisnesslogic.model.CreditCard
import buisnesslogic.model.Entries
import buisnesslogic.model.Entry
import buisnesslogic.model.Password
import buisnesslogic.model.PlainText
import com.google.gson.Gson

class EntriesStorageImpl(
  private val cryptography: Cryptography,
  private val fileSaver: FileSaver,
  private val gson: Gson
) : EntriesStorage {
  
  override suspend fun getEntries(masterPassword: String): Entries {
    val ciphertext = checkNotNull(fileSaver.readData()) { "Encrypted data was empty" }
    // Text in file should not be empty, because it should have been saved earlier
    require(ciphertext.isNotEmpty())
    val json = cryptography.decryptData(masterPassword, ciphertext)
    if (json == "") return Entries(emptyList(), emptyList(), emptyList())
    return gson.fromJson(json, Entries::class.java)
  }
  
  override suspend fun saveEntries(masterPassword: String, entries: Entries) {
    saveEntriesToFile(masterPassword, entries)
  }
  
  override suspend fun saveEntry(masterPassword: String, entry: Entry): Entries {
    val entries = getEntries(masterPassword)
    val newEntries = when (entry) {
      is Password -> entries.copy(passwords = entries.passwords + entry)
      is CreditCard -> entries.copy(creditCards = entries.creditCards + entry)
      is PlainText -> entries.copy(plainTexts = entries.plainTexts + entry)
    }
    saveEntriesToFile(masterPassword, newEntries)
    return newEntries
  }
  
  override suspend fun updateEntry(masterPassword: String, entry: Entry): Entries {
    val entries = getEntries(masterPassword)
    val newEntries = when (entry) {
      is Password -> entries.copy(
        passwords = entries.passwords.toMutableList()
            .apply { removeAll { it.id == entry.id }; add(entry) }
      )
      is CreditCard -> entries.copy(
        creditCards = entries.creditCards.toMutableList()
            .apply { removeAll { it.id == entry.id }; add(entry) }
      )
      is PlainText -> entries.copy(
        plainTexts = entries.plainTexts.toMutableList()
            .apply { removeAll { it.id == entry.id }; add(entry) }
      )
    }
    saveEntriesToFile(masterPassword, newEntries)
    return newEntries
  }
  
  override suspend fun deleteEntry(masterPassword: String, entry: Entry): Entries {
    val entries = getEntries(masterPassword)
    val newEntries = when (entry) {
      is Password -> entries.copy(
        passwords = entries.passwords.toMutableList().apply { removeAll { it.id == entry.id } }
      )
      is CreditCard -> entries.copy(
        creditCards = entries.creditCards.toMutableList().apply { removeAll { it.id == entry.id } }
      )
      is PlainText -> entries.copy(
        plainTexts = entries.plainTexts.toMutableList().apply { removeAll { it.id == entry.id } }
      )
    }
    saveEntriesToFile(masterPassword, newEntries)
    return newEntries
  }
  
  private suspend fun saveEntriesToFile(password: String, entries: Entries) {
    val passwordsInfoJson = gson.toJson(entries)
    val encryptedText = cryptography.encryptData(password, passwordsInfoJson)
    fileSaver.saveData(encryptedText)
  }
}
