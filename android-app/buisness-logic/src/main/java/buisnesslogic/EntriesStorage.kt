package buisnesslogic

import buisnesslogic.model.Entries
import buisnesslogic.model.Entry

/**
 * Storage that provides basic CRUD operations for [Entry]
 */
interface EntriesStorage {
  
  suspend fun getEntries(masterPassword: Password): Entries
  
  suspend fun saveEntries(masterPassword: Password, entries: Entries)
  
  suspend fun saveEntry(masterPassword: Password, entry: Entry): Entries
  
  suspend fun updateEntry(masterPassword: Password, entry: Entry): Entries
  
  suspend fun deleteEntry(masterPassword: Password, entry: Entry): Entries
}
