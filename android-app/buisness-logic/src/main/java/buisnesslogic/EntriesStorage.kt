package buisnesslogic

import buisnesslogic.model.Entries
import buisnesslogic.model.Entry

/**
 * Storage that provides basic CRUD operations for [Entry]
 */
interface EntriesStorage {
  
  suspend fun getEntries(masterPassword: String): Entries
  
  suspend fun saveEntries(masterPassword: String, entries: Entries)
  
  suspend fun saveEntry(masterPassword: String, entry: Entry): Entries
  
  suspend fun updateEntry(masterPassword: String, entry: Entry): Entries
  
  suspend fun deleteEntry(masterPassword: String, entry: Entry): Entries
}
