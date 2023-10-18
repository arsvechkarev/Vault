package com.arsvechkarev.vault.features.common.domain

import com.arsvechkarev.vault.features.common.data.preferences.Preferences

interface DatabaseChangesJournal {
  suspend fun markChange()
  suspend fun getChangeCount(): Int
}

class DatabaseChangesJournalImpl(
  private val preferences: Preferences
) : DatabaseChangesJournal {
  
  override suspend fun markChange() {
    var currentCount = preferences.getLong(KEY_DATABASE_CHANGES_COUNT)
    preferences.putLong(KEY_DATABASE_CHANGES_COUNT, ++currentCount)
  }
  
  override suspend fun getChangeCount(): Int {
    return preferences.getLong(KEY_DATABASE_CHANGES_COUNT).toInt()
  }
  
  private companion object {
    
    const val KEY_DATABASE_CHANGES_COUNT = "database_changes_count"
  }
}
