package com.arsvechkarev.vault.stubs

import com.arsvechkarev.vault.features.common.domain.DatabaseChangesJournal

class StubDatabaseChangesJournal : DatabaseChangesJournal {
  
  private var changes = 0
  
  override suspend fun markChange() {
    changes++
  }
  
  override suspend fun getChangeCount(): Int {
    return changes
  }
}
