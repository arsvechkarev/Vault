package com.arsvechkarev.vault.features.main_list.domain

import buisnesslogic.model.Entries
import buisnesslogic.model.Password
import com.arsvechkarev.vault.core.model.toPasswordItem
import com.arsvechkarev.vault.features.main_list.model.EntriesItems

class EntriesMapper {
  
  fun mapEntries(entries: Entries): EntriesItems {
    return EntriesItems(
      passwordsItems = entries.passwords
          .sortedBy { it.websiteName.lowercase() }
          .map(Password::toPasswordItem)
    )
  }
}