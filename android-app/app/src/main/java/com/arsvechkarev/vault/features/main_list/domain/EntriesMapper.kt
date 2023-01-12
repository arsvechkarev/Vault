package com.arsvechkarev.vault.features.main_list.domain

import buisnesslogic.model.Entries
import buisnesslogic.model.Password
import buisnesslogic.model.PlainText
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.common.model.Title
import com.arsvechkarev.vault.features.common.model.toPasswordItem
import com.arsvechkarev.vault.features.common.model.toPlainTextItem
import com.arsvechkarev.vault.recycler.DifferentiableItem

class EntriesMapper {
  
  fun mapEntries(entries: Entries): List<DifferentiableItem> {
    val plainTexts = entries.plainTexts
        .sortedBy { it.title.lowercase() }
        .map(PlainText::toPlainTextItem)
    val passwordsItems = entries.passwords
        .sortedBy { it.websiteName.lowercase() }
        .map(Password::toPasswordItem)
    return buildList {
      if (passwordsItems.isNotEmpty()) {
        add(Title(R.string.text_passwords))
      }
      addAll(passwordsItems)
      if (plainTexts.isNotEmpty()) {
        add(Title(R.string.text_plain_texts))
      }
      addAll(plainTexts)
    }
  }
}
